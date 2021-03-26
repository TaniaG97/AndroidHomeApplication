package com.example.androidhomeapplication.data.remote.response

import com.example.androidhomeapplication.Utils
import com.example.androidhomeapplication.data.models.Actor
import com.example.androidhomeapplication.data.models.Movie
import com.example.androidhomeapplication.data.models.MovieDetails
import kotlinx.serialization.*

@Serializable
data class MovieDetailsResponse(
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

fun MovieDetailsResponse.mapToMovieDetails(backdropURL: String, casts: List<Actor> = listOf()): MovieDetails =
    MovieDetails(
        movieBaseInfo = Movie(
            id = this.id.toLong(),
            ageLimit = Utils.getAgeLimit(this.adult),
            title = this.title,
            genres = this.genres.map { genreResponse -> genreResponse.mapToGenre() },
            reviewCount = this.voteCount,
            isLiked = false,
            rating = this.voteAverage.toInt(),
            imageUrl = backdropURL + this.backdropPath
        ),
        storyLine = this.overview,
        actors = casts
    )