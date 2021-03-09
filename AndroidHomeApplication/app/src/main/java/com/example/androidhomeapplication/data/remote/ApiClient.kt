package com.example.androidhomeapplication.data.remote

import com.example.androidhomeapplication.DataResult
import com.example.androidhomeapplication.data.models.Actor
import com.example.androidhomeapplication.data.models.Genre
import com.example.androidhomeapplication.data.models.Movie
import com.example.androidhomeapplication.data.models.MovieDetails
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

    private val movieService: MoviesService by lazy {
        retrofit.create(MoviesService::class.java)
    }
    private val genresService: GenresService by lazy {
        retrofit.create(GenresService::class.java)
    }

    private val searchService: SearchService by lazy {
        retrofit.create(SearchService::class.java)
    }

    suspend fun loadMovies(page:Int): List<Movie> {
        val genres = genresService.loadGenres().genres
        val movies = movieService.loadPopular(page = page).results

        return movies.map { movie ->
            Movie(
                id = movie.id,
                title = movie.title,
                imageUrl = BASE_POSTER_URL + movie.posterPath,
                rating = movie.voteAverage.toInt(),
                reviewCount = movie.voteCount,
                pgAge = if (movie.adult) 16 else 13,
                isLiked = false,
                genres = genres
                    .filter { genreResponse ->
                        movie.genreIDS.contains(genreResponse.id)
                    }
                    .map { genre ->
                        Genre(
                            genre.id,
                            genre.name
                        )
                    },

            )
        }
    }

    suspend fun loadMovie(movieId: Int): MovieDetails {
        val details = movieService.loadMovieDetails(movieId)
        val credits = movieService.loadMovieCredits(movieId)

        return MovieDetails(
            id = details.id,
            pgAge = if (details.adult) 16 else 13,
            title = details.title,
            genres = details.genres.map { Genre(it.id, it.name) },
            reviewCount = details.voteCount,
            isLiked = false,
            rating = details.voteAverage.toInt(),
            detailImageUrl = BASE_BACKDROP_URL + details.backdropPath,
            storyLine = details.overview,
            actors = credits.cast.map { actor ->
                Actor(
                    id = actor.id,
                    name = actor.name,
                    imageUrl = BASE_PROFILE_URL + actor.profilePath
                )
            }
        )
    }

    suspend fun searchMovies(queryString: String, page: Int): List<Movie> {
        val genres = genresService.loadGenres().genres
        val movies = searchService.loadPopular(queryString, page = page).results

        return movies.map { movie ->
            Movie(
                id = movie.id,
                title = movie.title,
                imageUrl = BASE_POSTER_URL + movie.posterPath,
                rating = movie.voteAverage.toInt(),
                reviewCount = movie.voteCount,
                pgAge = if (movie.adult) 16 else 13,
                isLiked = false,
                genres = genres
                    .filter { genreResponse ->
                        movie.genreIDS.contains(genreResponse.id)
                    }
                    .map { genre ->
                        Genre(
                            genre.id,
                            genre.name
                        )
                    },

                )
        }
    }
}