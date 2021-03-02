package com.example.androidhomeapplication.data.repository

import com.example.androidhomeapplication.data.models.Movie
import com.example.androidhomeapplication.data.models.MovieDetails

interface BaseRepository {
    suspend fun getMovies(): List<Movie>
    suspend fun getMovie(movieId: Int): MovieDetails?
}