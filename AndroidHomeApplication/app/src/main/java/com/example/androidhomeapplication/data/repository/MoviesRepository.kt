package com.example.androidhomeapplication.data.repository

import com.example.androidhomeapplication.utils.DataResult
import com.example.androidhomeapplication.data.room.mapToMovie
import com.example.androidhomeapplication.data.models.Genre
import com.example.androidhomeapplication.data.models.Movie
import com.example.androidhomeapplication.data.models.MovieDetails
import com.example.androidhomeapplication.data.remote.response.*
import com.example.androidhomeapplication.data.remote.services.ConfigurationService
import com.example.androidhomeapplication.data.remote.services.MoviesService
import com.example.androidhomeapplication.data.room.MovieDatabase
import com.example.androidhomeapplication.data.room.mapToMovieDetails
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
    private val db: MovieDatabase,
    private val movieService: MoviesService,
    private val configurationService: ConfigurationService
) {
    private val mutex = Mutex()
    private var genres: Map<Long, Genre>? = null
    private var configInfo: ConfigurationResponse? = null

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
        details.await().mapToMovieDetails(backdropImageUrl, casts.await())
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

    suspend fun getCachedMovies():List<Movie> =
        db.movieDao().getPopularMovies().map { movieWithGenres -> movieWithGenres.mapToMovie() }

    suspend fun getCachedMovieById(movieId: Long): MovieDetails? =
        db.movieDao().getMovieWithGenresAndActorsById(movieId)?.mapToMovieDetails()

    suspend fun saveMoviesToCache(movies: List<Movie>){
        db.movieDao().saveMovies(movies)
    }

    suspend fun saveMovieDetailsToCache(movieDetails: MovieDetails){
        db.movieDao().saveMovieDetailsItem(movieDetails)
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