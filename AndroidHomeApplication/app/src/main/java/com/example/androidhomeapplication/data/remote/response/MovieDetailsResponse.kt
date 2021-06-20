package com.example.androidhomeapplication.data.remote.response

import android.os.Parcelable
import com.example.androidhomeapplication.Utils
import com.example.androidhomeapplication.data.db.MovieDbEntity
import com.example.androidhomeapplication.data.models.Actor
import com.example.androidhomeapplication.data.models.Genre
import com.example.androidhomeapplication.data.models.Movie
import com.example.androidhomeapplication.data.models.MovieDetails
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieDetailsResponse(
    @Json(name = "adult")
    val adult: Boolean,

    @Json(name = "backdrop_path")
    val backdropPath: String?,

    @Json(name = "budget")
    val budget: Int,

    @Json(name = "genres")
    val genres: List<GenreResponse>,

    @Json(name = "homepage")
    val homepage: String?,

    @Json(name = "id")
    val id: Int,

    @Json(name = "imdb_id")
    val imdbID: String,

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
    val releaseDate: String,

    @Json(name = "runtime")
    val runtime: Long,

    @Json(name = "status")
    val status: String,

    @Json(name = "tagline")
    val tagLine: String?,

    @Json(name = "title")
    val title: String,

    @Json(name = "video")
    val video: Boolean,

    @Json(name = "vote_average")
    val voteAverage: Double,

    @Json(name = "vote_count")
    val voteCount: Long
) : Parcelable

fun MovieDetailsResponse.mapToMovieDetails(backdropUrl: String, casts: List<Actor>): MovieDetails =
    MovieDetails(
        movieBaseInfo = Movie(
            id = this.id.toLong(),
            ageLimit = Utils.getAgeLimit(this.adult),
            title = this.title,
            genres = this.genres.map { genreResponse -> genreResponse.mapToGenre() },
            reviewCount = this.voteCount,
            isLiked = false,
            rating = this.voteAverage.toInt(),
            imageUrl = backdropUrl + this.backdropPath,
            popularity = this.popularity
        ),
        storyLine = this.overview,
        actors = casts
    )

//fun MovieDetailsResponse.mapToMovieDbEntity(backdropUrl: String, casts: List<Actor>): MovieDbEntity =
//    MovieDbEntity(
//        id = this.id.toLong(),
//        title = this.title,
//        imageUrl = backdropUrl + this.posterPath,
//        rating = this.voteAverage.toInt(),
//        reviewCount = this.voteCount,
//        ageLimit = Utils.getAgeLimit(this.adult),
//        isLiked = false,
//        genres = this.genres.map { genreResponse -> genreResponse.mapToGenre() },
//        storyLine = this.overview,
//        actors = casts
//    )