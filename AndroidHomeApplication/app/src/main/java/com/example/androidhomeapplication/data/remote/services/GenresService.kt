package com.example.androidhomeapplication.data.remote.services

import com.example.androidhomeapplication.data.remote.response.GenresResponse
import retrofit2.http.GET

interface GenresService {
    @GET("genre/movie/list")
    suspend fun loadGenres(): GenresResponse
}