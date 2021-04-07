package com.example.androidhomeapplication.data.repository

import com.example.androidhomeapplication.data.models.Genre
import com.example.androidhomeapplication.data.models.Movie
import com.example.androidhomeapplication.data.models.MovieDetails
import com.example.androidhomeapplication.data.remote.RetrofitBuilder
import com.example.androidhomeapplication.data.remote.response.*
import com.example.androidhomeapplication.data.remote.services.ConfigurationService
import com.example.androidhomeapplication.data.remote.services.MoviesService
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import retrofit2.Retrofit

private const val SECURE_BASE_URL: String = "https://image.tmdb.org/t/p/"

enum class ImageType(val size: String) {
    POSTER(size = "w500"),
    BACKDROP(size = "w780"),
    PROFILE(size = "w185")
}

class MoviesRepository(
    private val movieService: MoviesService,
    private val configurationService: ConfigurationService
) {


    private val mutex = Mutex()
    private var genres: Map<Long, Genre>? = null
    private var configInfo: ConfigurationResponse? = null

    suspend fun loadMovieById(movieId: Long): MovieDetails = coroutineScope {
        val configurationInfo = async { getCachedConfigInfoOrLoad() }
        val configurationInfoValue = configurationInfo.await()

        val details = async { movieService.loadMovieDetails(movieId) }
        val casts = async {
            movieService.loadMovieCredits(movieId).cast.map { castResponse ->
                castResponse.mapToActor(configurationInfoValue.getImageUrlByType(ImageType.PROFILE))
            }
        }

        details.await().mapToMovieDetails(
            configurationInfoValue.getImageUrlByType(ImageType.BACKDROP),
            casts.await()
        )
    }

    suspend fun loadMovies(queryString: String?, page: Int): List<Movie> = coroutineScope {
        val movies = async { selectApiCallForMoviesData(queryString, page) }
        val genresMap = async { getCachedGenresOrLoad() }
        val configurationInfo = async { getCachedConfigInfoOrLoad() }

        val posterImageUrl = configurationInfo.await().getImageUrlByType(ImageType.POSTER)
        val genresMapValue = genresMap.await()
        movies.await().map { movie ->
            val movieGenres =
                movie.genreIds.mapNotNull { singleGenreId -> genresMapValue[singleGenreId] }
            movie.mapToMovie(posterImageUrl, movieGenres)
        }
    }

    private suspend fun selectApiCallForMoviesData(
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