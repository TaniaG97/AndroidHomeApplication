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

class MoviesRepository() {
    private val retrofit: Retrofit = RetrofitBuilder.buildRetrofit()
    private val configurationService: ConfigurationService by lazy {
        retrofit.create(
            ConfigurationService::class.java
        )
    }
    private val movieService: MoviesService by lazy { retrofit.create(MoviesService::class.java) }

    private val mutex = Mutex()
    private var genres: Map<Long, Genre>? = null
    private var configInfo: ConfigurationResponse? = null

    suspend fun loadMovieById(movieId: Long): MovieDetails = coroutineScope {
        val profileImageUrl = getImageUrlByType(ImageType.PROFILE)
        val backdropImageUrl = getImageUrlByType(ImageType.BACKDROP)

        val details = async { movieService.loadMovieDetails(movieId) }
        val casts = async {
            movieService.loadMovieCredits(movieId).cast.map { castResponse ->
                castResponse.mapToActor(profileImageUrl)
            }
        }

        details.await().mapToMovieDetails(backdropImageUrl, casts.await())
    }

    suspend fun loadMovies(queryString: String?, page: Int): List<Movie> = coroutineScope {
        val movies = async { loadData(queryString, page) }
        val genresMap = async { getCachedGenresOrLoad() }
        val posterImageUrl = getImageUrlByType(ImageType.POSTER)

        movies.await().map { movie ->
            val movieGenres =
                movie.genreIds.mapNotNull { singleGenreId -> genresMap.await()[singleGenreId] }
            movie.mapToMovie(posterImageUrl, movieGenres)
        }
    }

    private suspend fun loadData(
        queryString: String?,
        page: Int
    ) = if (queryString.isNullOrEmpty()) {
        movieService.loadPopular(page = page)
    } else {
        movieService.searchMovie(query = queryString, page = page)
    }.results

    private suspend fun getCachedGenresOrLoad(): Map<Long, Genre> = mutex.withLock {
        val value = genres
        if (value != null) {
            value
        } else {
            val newInfo = movieService.loadGenres().genres.associateBy({ genres -> genres.id },
                { genreResponse -> genreResponse.mapToGenre() })
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


    private suspend fun getImageUrlByType(imageType: ImageType): String {
        val configuration = getCachedConfigInfoOrLoad()

        val secureBaseURL = configuration?.images?.secureBaseUrl ?: SECURE_BASE_URL
        val imageSize = when (imageType) {
            ImageType.POSTER -> configuration?.images?.posterSizes?.last()
                ?: "${ImageType.POSTER.size}/"
            ImageType.BACKDROP -> configuration?.images?.backdropSizes?.last()
                ?: "${ImageType.BACKDROP.size}/"
            ImageType.PROFILE -> configuration?.images?.profileSizes?.last()
                ?: "${ImageType.PROFILE.size}/"
        }

        return secureBaseURL + imageSize
    }
}