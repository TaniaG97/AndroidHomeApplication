package com.example.androidhomeapplication.data.remote.services

import com.example.androidhomeapplication.data.remote.response.ConfigurationResponse
import com.example.androidhomeapplication.data.remote.response.MovieCreditsResponse
import com.example.androidhomeapplication.data.remote.response.MovieDetailsResponse
import com.example.androidhomeapplication.data.remote.response.PopularMoviesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesService {
    @GET("movie/popular")
    suspend fun loadPopular(
        @Query("page") page: Int
    ): PopularMoviesResponse

    @GET("movie/{movie_id}")
    suspend fun loadMovieDetails(
        @Path("movie_id") movieId: Int
    ): MovieDetailsResponse

    @GET("movie/{movie_id}/credits")
    suspend fun loadMovieCredits(
        @Path("movie_id") movieId: Int
    ): MovieCreditsResponse
}