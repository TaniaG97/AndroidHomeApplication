package com.example.androidhomeapplication.data.remote.services

import com.example.androidhomeapplication.data.remote.response.MoviesListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {
    @GET("search/movie")
    suspend fun loadPopular(
        @Query("query") query: String,
        @Query("page") page: Int
    ): MoviesListResponse
}