package com.example.androidhomeapplication.data

import com.example.androidhomeapplication.models.Actor
import com.example.androidhomeapplication.models.Genre
import com.example.androidhomeapplication.models.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class JsonMovie(
    val id: Long,
    val title: String,
    @SerialName("poster_path")
    val posterPicture: String,
    @SerialName("backdrop_path")
    val backdropPicture: String,
    val runtime: Int,
    @SerialName("genre_ids")
    val genreIds: List<Long>,
    val actors: List<Long>,
    @SerialName("vote_average")
    val ratings: Float,
    @SerialName("vote_count")
    val votesCount: Int,
    val overview: String,
    val adult: Boolean
)

internal fun JsonMovie.mapToMovie(genresMap: Map<Long, Genre>, actorsMap: Map<Long, Actor>): Movie =
    Movie(
        id = this.id,
        title = this.title,
        storyLine = this.overview,
        imageUrl = this.posterPicture,
        detailImageUrl = this.backdropPicture,
        rating = (this.ratings / 2).toInt(),
        reviewCount = this.votesCount,
        pgAge = if (this.adult) 16 else 13,
        runningTime = this.runtime,
        genres = this.genreIds.mapNotNull { id -> genresMap[id] },
        actors = this.actors.mapNotNull { id -> actorsMap[id] },
        isLiked = false
    )