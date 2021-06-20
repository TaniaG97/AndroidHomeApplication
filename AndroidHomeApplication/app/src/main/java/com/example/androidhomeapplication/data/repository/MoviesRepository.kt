package com.example.androidhomeapplication.data.repository

import com.example.androidhomeapplication.DataResult
import com.example.androidhomeapplication.data.db.FilmDatabase
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

    private val moviePageToLoad = MutableStateFlow(1)
    private val queryForSearch = MutableStateFlow("")

    val loadMoviePageFlow: Flow<DataResult<Int>> = moviePageToLoad
        .flatMapLatest { page ->
            flow {
                emit(DataResult.Loading())

                val movies = loadMovies("", page)
                if (!movies.isNullOrEmpty()) {
                    if (page == 1) {
                        db.filmDao().clearTable()
                    }
                    db.filmDao().insertAll(movies.map { movie -> movie.mapToMovieDbEntity() })
                } else {
                    emit(DataResult.Error(Throwable("Some Error Message")))
                }

                emit(DataResult.Success(page))
            }
        }

    val searchFlow: Flow<DataResult<List<Movie>>> =
        moviePageToLoad.combine(queryForSearch) { page, query ->
            val movies = loadMovies(query, page)
            if (!movies.isNullOrEmpty()) {
                DataResult.Success(movies)
            } else {
                DataResult.Error(Throwable("Some Error Message"))
            }
        }

    val popularMoviesFlow: Flow<DataResult<List<Movie>>> = db.filmDao().getPopularMoviesFlow()
        .map { movieDbEntity ->
            val movies = movieDbEntity.map { movieDbEntity -> movieDbEntity.mapToMovie() }
            DataResult.Success(movies)
        }


    suspend fun loadMoviePage(pageId: Int) {
        moviePageToLoad.emit(pageId)
    }

    suspend fun emitSearchQuery(query: String) {
        queryForSearch.emit(query)
    }

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

    suspend fun loadMovies(queryString: String?, page: Int): List<Movie> = coroutineScope {
        val configurationInfo = async { getCachedConfigInfoOrLoad() }
        val movies = async { getMovieDataSource(queryString, page) }
        val genresMap = async { getCachedGenresOrLoad() }

        val posterImageUrl = configurationInfo.await().getImageUrlByType(ImageType.POSTER)
        val genresMapValue = genresMap.await()
        movies.await().map { movie ->
            val movieGenres =
                movie.genreIds.mapNotNull { singleGenreId -> genresMapValue[singleGenreId] }
            movie.mapToMovie(posterImageUrl, movieGenres)
        }
    }

    private suspend fun getMovieDataSource(
        queryString: String?,
        page: Int
    ): List<MovieResponse> = if (queryString.isNullOrEmpty()) {
        movieService.loadPopular(page = page)
    } else {
        movieService.searchMovie(query = queryString, page = page)
    }.results


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