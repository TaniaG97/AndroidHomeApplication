package com.example.androidhomeapplication.data.remote.response

import android.os.Parcelable
import com.example.androidhomeapplication.utils.Utils
import com.example.androidhomeapplication.data.models.Genre
import com.example.androidhomeapplication.data.models.Movie
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieResponse(
    @Json(name = "adult")
    val adult: Boolean,

    @Json(name = "backdrop_path")
    val backdropPath: String?,

    @Json(name = "genre_ids")
    val genreIds: List<Long>,

    @Json(name = "id")
    val id: Int,

    @Json(name = "original_language")
    val originalLanguage: String,

    @Json(name = "original_title")
    val originalTitle: String,

    @Json(name = "overview")
    val overview: String,

    @Json(name = "popularity")
    val popularity: Double,

    @Json(name = "poster_path")
    val posterPath: String?,

    @Json(name = "release_date")
    val releaseDate: String?,

    @Json(name = "title")
    val title: String,

    @Json(name = "video")
    val video: Boolean,

    @Json(name = "vote_average")
    val voteAverage: Double,

    @Json(name = "vote_count")
    val voteCount: Long
) : Parcelable

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
        popularity = this.popularity
    )