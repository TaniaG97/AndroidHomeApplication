package com.example.androidhomeapplication.data.repository

import com.example.androidhomeapplication.data.remote.ApiClient
import com.example.androidhomeapplication.data.models.Movie
import com.example.androidhomeapplication.data.models.MovieDetails

class MoviesRepository(
    private val apiClient: ApiClient
) {

    suspend fun getMovies(page:Int): List<Movie> {
        return apiClient.loadMovies(page)
    }

    suspend fun getMovie(movieId: Long): MovieDetails {
        return apiClient.loadMovie(movieId)
    }

    suspend fun searchMovies(queryString: String, page: Int): List<Movie> {
        return apiClient.searchMovies(queryString, page)
    }
}