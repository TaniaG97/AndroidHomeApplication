package com.example.androidhomeapplication.data.remote.services

import com.example.androidhomeapplication.data.remote.response.GenresResponse
import com.example.androidhomeapplication.data.remote.response.MovieCreditsResponse
import com.example.androidhomeapplication.data.remote.response.MovieDetailsResponse
import com.example.androidhomeapplication.data.remote.response.MoviesListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesService {
    @GET("movie/popular")
    suspend fun loadPopular(
        @Query("page") page: Int
    ): MoviesListResponse

    @GET("movie/{movie_id}")
    suspend fun loadMovieDetails(
        @Path("movie_id") movieId: Long
    ): MovieDetailsResponse

    @GET("movie/{movie_id}/credits")
    suspend fun loadMovieCredits(
        @Path("movie_id") movieId: Long
    ): MovieCreditsResponse

    @GET("genre/movie/list")
    suspend fun loadGenres(): GenresResponse

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") query: String?,
        @Query("page") page: Int
    ): MoviesListResponse
}