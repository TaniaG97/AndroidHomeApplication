package com.example.androidhomeapplication.data.repository

import com.example.androidhomeapplication.data.remote.ApiClient
import com.example.androidhomeapplication.data.models.Movie
import com.example.androidhomeapplication.data.models.MovieDetails

class MoviesRepository(
    private val apiClient: ApiClient
) : BaseRepository {

    override suspend fun getMovies(): List<Movie> {
        return apiClient.loadMovies()
    }

    override suspend fun getMovie(movieId: Int): MovieDetails {
        return apiClient.loadMovie(movieId)
    }
}