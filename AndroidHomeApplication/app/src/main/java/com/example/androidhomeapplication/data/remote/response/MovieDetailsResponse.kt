package com.example.androidhomeapplication.data.remote.response

import kotlinx.serialization.*
import kotlinx.serialization.json.JsonObject

@Serializable
data class MovieDetailsResponse (
    @SerialName("adult")
    val adult: Boolean,

    @SerialName("backdrop_path")
    val backdropPath: String?,

    @SerialName("budget")
    val budget: Int,

    @SerialName("genres")
    val genres: List<GenreResponse>,

    @SerialName("homepage")
    val homepage: String?,

    @SerialName("id")
    val id: Int,

    @SerialName("imdb_id")
    val imdbID: String,

    @SerialName("original_language")
    val originalLanguage: String,

    @SerialName("original_title")
    val originalTitle: String,

    @SerialName("overview")
    val overview: String,

    @SerialName("popularity")
    val popularity: Double,

    @SerialName("poster_path")
    val posterPath: String?,

    @SerialName("release_date")
    val releaseDate: String,

    @SerialName("runtime")
    val runtime: Long,

    @SerialName("status")
    val status: String,

    @SerialName("tagline")
    val tagLine: String?,

    @SerialName("title")
    val title: String,

    @SerialName("video")
    val video: Boolean,

    @SerialName("vote_average")
    val voteAverage: Double,

    @SerialName("vote_count")
    val voteCount: Long
)