package com.example.androidhomeapplication.data.repository

import com.example.androidhomeapplication.data.models.Genre
import com.example.androidhomeapplication.data.models.Movie
import com.example.androidhomeapplication.data.models.MovieDetails
import com.example.androidhomeapplication.data.remote.*
import com.example.androidhomeapplication.data.remote.response.*
import com.example.androidhomeapplication.data.remote.services.ConfigurationService
import com.example.androidhomeapplication.data.remote.services.MoviesService
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import retrofit2.Retrofit

private const val SECURE_BASE_URL = "https://image.tmdb.org/t/p/"
private const val BASE_POSTER_SIZE = "w500"
private const val BASE_BACKDROP_SIZE = "w780"
private const val BASE_PROFILE_SIZE = "w185"

enum class ImageType {
    POSTER,
    BACKDROP,
    PROFILE
}

class MoviesRepository(
    private val retrofit: Retrofit,
    private val configurationService: ConfigurationService,
    private val movieService: MoviesService
) {
    private val mutex = Mutex()
    private var genres: Map<Long, Genre> = mapOf()
    private var configInfo: ConfigurationResponse? = null

    suspend fun loadMovies(page: Int): List<Movie> {
        val movies = movieService.loadPopular(page = page).results
        val genersMap = getCachedGenresOrLoad()
        return movies.map { movie ->
            val movieGenres = mutableListOf<Genre>()
            movie.genreIDS.forEach { genreId -> movieGenres.add(genersMap.getValue(genreId)) }
            movie.mapToMovie(getImageUrlByType(ImageType.POSTER), movieGenres)
        }
    }

    suspend fun loadMovie(movieId: Long): MovieDetails {
        val details = movieService.loadMovieDetails(movieId)
        val casts = movieService.loadMovieCredits(movieId).cast.map { castResponse ->
            castResponse.mapToActor(getImageUrlByType(ImageType.PROFILE))
        }

        return details.mapToMovieDetails(getImageUrlByType(ImageType.BACKDROP), casts)
    }

    suspend fun searchMovies(queryString: String, page: Int): List<Movie> {
        val movies = movieService.searchMovie(query = queryString, page = page).results
        val genersMap = getCachedGenresOrLoad()
        return movies.map { movie ->
            val movieGenres = mutableListOf<Genre>()
            movie.genreIDS.forEach { genreId -> movieGenres.add(genersMap.getValue(genreId)) }
            movie.mapToMovie(getImageUrlByType(ImageType.POSTER), movieGenres)
        }
    }

    suspend fun getCachedGenresOrLoad(): Map<Long, Genre> {
        mutex.withLock {
            if (genres.isEmpty()) {
                genres =
                    movieService.loadGenres().genres.associateBy({ it.id }, { it.mapToGenre() })
            }
        }
        return genres
    }

    suspend fun getCachedConfigInfoOrLoad(): ConfigurationResponse? {
        mutex.withLock {
            if (configInfo == null) {
                configInfo = configurationService.loadConfiguration()
            }
        }
        return configInfo
    }

    suspend fun getImageUrlByType(imageType: ImageType): String {
        val configuration = getCachedConfigInfoOrLoad()

        val secureBaseURL = configuration?.images?.secureBaseURL ?: SECURE_BASE_URL
        val imageSize = when (imageType) {
            ImageType.POSTER -> configuration?.images?.posterSizes?.last() ?: "$BASE_POSTER_SIZE/"
            ImageType.BACKDROP -> configuration?.images?.backdropSizes?.last() ?: "$BASE_BACKDROP_SIZE/"
            ImageType.PROFILE -> configuration?.images?.profileSizes?.last() ?: "$BASE_PROFILE_SIZE/"
        }

        return secureBaseURL + imageSize
    }
}