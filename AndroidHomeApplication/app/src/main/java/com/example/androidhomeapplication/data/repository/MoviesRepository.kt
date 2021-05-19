package com.example.androidhomeapplication.data.repository

import com.example.androidhomeapplication.DataResult
import com.example.androidhomeapplication.data.db.FilmDatabase
import com.example.androidhomeapplication.data.db.MovieDbEntity
import com.example.androidhomeapplication.data.db.mapToMovie
import com.example.androidhomeapplication.data.models.Genre
import com.example.androidhomeapplication.data.models.Movie
import com.example.androidhomeapplication.data.models.MovieDetails
import com.example.androidhomeapplication.data.models.mapToMovieDbEntity
import com.example.androidhomeapplication.data.remote.response.*
import com.example.androidhomeapplication.data.remote.services.ConfigurationService
import com.example.androidhomeapplication.data.remote.services.MoviesService
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

private const val SECURE_BASE_URL: String = "https://image.tmdb.org/t/p/"

enum class ImageType(val size: String) {
    POSTER(size = "w500"),
    BACKDROP(size = "w780"),
    PROFILE(size = "w185")
}

class MoviesRepository(
    private val db: FilmDatabase,
    private val movieService: MoviesService,
    private val configurationService: ConfigurationService
) {
    private val mutex = Mutex()
    private var genres: Map<Long, Genre>? = null
    private var configInfo: ConfigurationResponse? = null

    private val moviePageToLoad = MutableSharedFlow<Int>()

    private val cachingMoviesFlow =
        moviePageToLoad
            .flatMapLatest { page ->
                fetchWithCachingFlow(
                    databaseQuery = {
                        db.filmDao().getMovies().map { movieDbEntity -> movieDbEntity.mapToMovie() }
                    },
                    networkCall = {loadMovies(page)},
                    saveCallResult = { data ->
                        if (page==1){
                            db.filmDao().clearTable()
                        }
                        db.filmDao().insertAll(data.map { movie -> movie.mapToMovieDbEntity() })
                    }
                )
            }

    private val dbMoviesFlow = db.filmDao().getMoviesFlow()
        .map { movieDbEntityList ->
            DataResult.Success(movieDbEntityList.map { movieDbEntity -> movieDbEntity.mapToMovie() })
        }

    val moviesFlow: Flow<DataResult<List<Movie>>> = merge(cachingMoviesFlow, dbMoviesFlow)
        .distinctUntilChanged()

    suspend fun loadMovieById(movieId: Long): MovieDetails = coroutineScope {
        val configurationInfo = async { getCachedConfigInfoOrLoad() }
        val details = async { movieService.loadMovieDetails(movieId) }

        val configurationInfoValue = configurationInfo.await()
        val profileImageUrl = configurationInfoValue.getImageUrlByType(ImageType.PROFILE)
        val backdropImageUrl = configurationInfoValue.getImageUrlByType(ImageType.BACKDROP)

        val casts = async {
            movieService.loadMovieCredits(movieId).cast.map { castResponse ->
                castResponse.mapToActor(profileImageUrl)
            }
        }

        details.await().mapToMovieDetails(
            backdropImageUrl,
            casts.await()
        )
    }

    suspend fun loadMovies(page: Int): List<Movie> = coroutineScope {
        val configurationInfo = async { getCachedConfigInfoOrLoad() }
        val movies = async { movieService.loadPopular(page = page).results }
        val genresMap = async { getCachedGenresOrLoad() }

        val posterImageUrl = configurationInfo.await().getImageUrlByType(ImageType.POSTER)
        val genresMapValue = genresMap.await()
        movies.await().map { movie ->
            val movieGenres =
                movie.genreIds.mapNotNull { singleGenreId -> genresMapValue[singleGenreId] }
            movie.mapToMovie(posterImageUrl, movieGenres)
        }
    }

    suspend fun loadMoviePage(pageId: Int){
        moviePageToLoad.emit(pageId)
    }

//    private suspend fun getMovieDataSource(
//        queryString: String?,
//        page: Int
//    ): List<MovieResponse> = if (queryString.isNullOrEmpty()) {
//        movieService.loadPopular(page = page)
//    } else {
//        movieService.searchMovie(query = queryString, page = page)
//    }.results


    private suspend fun getCachedGenresOrLoad(): Map<Long, Genre> = mutex.withLock {
        val value = genres
        if (value != null) {
            value
        } else {
            val newInfo = movieService.loadGenres().genres.associateBy(
                { genres -> genres.id },
                { genreResponse -> genreResponse.mapToGenre() }
            )
            genres = newInfo
            newInfo
        }
    }

    private suspend fun getCachedConfigInfoOrLoad(): ConfigurationResponse = mutex.withLock {
        val value = configInfo
        if (value != null) {
            value
        } else {
            val newInfo = configurationService.loadConfiguration()
            configInfo = newInfo
            newInfo
        }
    }
}

fun <T> fetchWithCachingFlow(
    databaseQuery: suspend () -> T,
    networkCall: suspend () -> T,
    saveCallResult: suspend (T) -> Unit
): Flow<DataResult<T>> =
    flow {
        emit(DataResult.Loading())
        val cachedResult = DataResult.Success(databaseQuery.invoke())
        emit(cachedResult)

        try {
            val response = DataResult.Success(networkCall.invoke())
            saveCallResult(response.value)
            emit(response)
        } catch (throwable: Throwable) {
            val error = DataResult.Error(throwable)
            emit(error)
            emit(cachedResult)
        }
    }

