package com.example.androidhomeapplication.data.remote.response

import com.example.androidhomeapplication.Utils
import com.example.androidhomeapplication.data.models.Genre
import com.example.androidhomeapplication.data.models.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieResponse(
    @SerialName("adult")
    val adult: Boolean,

    @SerialName("backdrop_path")
    val backdropPath: String?,

    @SerialName("genre_ids")
    val genreIds: List<Long>,

    @SerialName("id")
    val id: Int,

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

    @SerialName("title")
    val title: String,

    @SerialName("video")
    val video: Boolean,

    @SerialName("vote_average")
    val voteAverage: Double,

    @SerialName("vote_count")
    val voteCount: Long
)

fun MovieResponse.mapToMovie(posterUrl: String, genres: List<Genre>): Movie =
    Movie(
        id = this.id.toLong(),
        title = this.title,
        imageUrl = posterUrl + this.posterPath,
        rating = this.voteAverage.toInt(),
        reviewCount = this.voteCount,
        ageLimit = Utils.getAgeLimit(this.adult),
        isLiked = false,
        genres = genres,
        )