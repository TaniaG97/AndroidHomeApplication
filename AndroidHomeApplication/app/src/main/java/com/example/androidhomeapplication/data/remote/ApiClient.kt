package com.example.androidhomeapplication.data.remote

import com.example.androidhomeapplication.DataResult
import com.example.androidhomeapplication.data.models.Actor
import com.example.androidhomeapplication.data.models.Genre
import com.example.androidhomeapplication.data.models.Movie
import com.example.androidhomeapplication.data.models.MovieDetails
import com.example.androidhomeapplication.data.remote.response.*
import com.example.androidhomeapplication.data.remote.services.ConfigurationService
import com.example.androidhomeapplication.data.remote.services.GenresService
import com.example.androidhomeapplication.data.remote.services.MoviesService
import com.example.androidhomeapplication.data.remote.services.SearchService
import kotlinx.coroutines.*
import retrofit2.Retrofit


private const val BASE_POSTER_URL = "https://image.tmdb.org/t/p/w500/"
private const val BASE_BACKDROP_URL = "https://image.tmdb.org/t/p/w780/"
private const val BASE_PROFILE_URL = "https://image.tmdb.org/t/p/w185/"

class ApiClient(
    private val retrofit: Retrofit
) {

    private val configurationService: ConfigurationService by lazy {
        retrofit.create(ConfigurationService::class.java)
    }
    private val movieService: MoviesService by lazy {
        retrofit.create(MoviesService::class.java)
    }
    private val genresService: GenresService by lazy {
        retrofit.create(GenresService::class.java)
    }
    private val searchService: SearchService by lazy {
        retrofit.create(SearchService::class.java)
    }

    private var config: ConfigurationResponse? = null
    private var genres: GenresResponse? = null

    suspend fun loadConfigurationSettings() {
        config = configurationService.loadConfiguration()
        genres = genresService.loadGenres()
    }

    suspend fun loadMovies(page: Int): List<Movie> {
        val movies = movieService.loadPopular(page = page).results
        if (genres == null) {
            loadConfigurationSettings()
        }

        return movies.map { movie ->
            val movieGenres = genres!!.genres
                .filter { genreResponse ->
                    movie.genreIDS.contains(genreResponse.id)
                }
                .map { genre ->
                    genre.mapToGenre()
                }
            movie.mapToMovie(BASE_POSTER_URL, movieGenres)
        }
    }

    suspend fun loadMovie(movieId: Int): MovieDetails {
        val details = movieService.loadMovieDetails(movieId)
        val casts = movieService.loadMovieCredits(movieId).cast.map { castResponse ->
            castResponse.mapToActor(BASE_PROFILE_URL)
        }

        return details.mapToMovieDetails(BASE_BACKDROP_URL, casts)
    }

    suspend fun searchMovies(queryString: String, page: Int): List<Movie> {
        val movies = searchService.loadPopular(query = queryString, page = page).results

        if (genres == null) {
            loadConfigurationSettings()
        }

        return movies.map { movie ->
            val movieGenres = genres!!.genres
                .filter { genreResponse ->
                    movie.genreIDS.contains(genreResponse.id)
                }
                .map { genre ->
                    genre.mapToGenre()
                }
            movie.mapToMovie(BASE_POSTER_URL, movieGenres)
        }
    }
}