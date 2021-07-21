package com.example.androidhomeapplication.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MoviesListResponse(
    @SerialName("page")
    val page: Long,

    @SerialName("results")
    val results: List<MovieResponse>,

    @SerialName("total_results")
    val totalResults: Long,

    @SerialName("total_pages")
    val totalPages: Long
)