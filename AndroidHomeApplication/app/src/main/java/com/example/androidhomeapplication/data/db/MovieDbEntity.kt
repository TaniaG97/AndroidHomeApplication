package com.example.androidhomeapplication.data.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.androidhomeapplication.Utils
import com.example.androidhomeapplication.data.models.*
import com.example.androidhomeapplication.data.remote.response.MovieDetailsResponse
import com.example.androidhomeapplication.data.remote.response.MovieResponse
import com.example.androidhomeapplication.data.remote.response.mapToGenre
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "movies")
@TypeConverters(RoomConverters::class)
data class MovieDbEntity(
    @PrimaryKey
    val id: Long,
    val ageLimit: Int,
    val title: String,
    val genres: List<Genre>,
    val reviewCount: Long,
    val isLiked: Boolean,
    val rating: Int,
    val imageUrl: String,
    val storyLine: String,
    val actors: List<Actor>,
    val popularity: Double
): Parcelable

fun MovieDbEntity.mapToMovie(): Movie =
    Movie(
        id = this.id,
        title = this.title,
        imageUrl = this.imageUrl,
        rating = this.rating,
        reviewCount = this.reviewCount,
        ageLimit = this.ageLimit,
        isLiked = this.isLiked,
        genres = this.genres,
        popularity = this.popularity
    )

fun MovieDbEntity.mapToMovieDetails(): MovieDetails =
    MovieDetails(
        movieBaseInfo = Movie(
            id = this.id,
            title = this.title,
            imageUrl = this.imageUrl,
            rating = this.rating,
            reviewCount = this.reviewCount,
            ageLimit = this.ageLimit,
            isLiked = this.isLiked,
            genres = this.genres,
            popularity = this.popularity
        ),
        storyLine = this.storyLine,
        actors = this.actors
    )