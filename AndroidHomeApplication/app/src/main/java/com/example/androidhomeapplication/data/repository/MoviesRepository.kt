package com.example.androidhomeapplication.data.repository

import com.example.androidhomeapplication.data.remote.ApiClient
import com.example.androidhomeapplication.data.models.Movie
import com.example.androidhomeapplication.data.models.MovieDetails

class MoviesRepository(
    private val apiClient: ApiClient
) {

    suspend fun getMovies(): List<Movie> {
        return apiClient.loadMovies()
    }

    suspend fun getMovie(movieId: Int): MovieDetails {
        return apiClient.loadMovie(movieId)
    }

    suspend fun searchMovies(searchValue: String): List<Movie> {
        return apiClient.searchMovies(searchValue)
    }
}