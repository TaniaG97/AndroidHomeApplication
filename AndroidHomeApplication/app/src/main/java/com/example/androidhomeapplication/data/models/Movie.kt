package com.example.androidhomeapplication.data.models

import com.example.androidhomeapplication.data.room.MovieEntity

data class Movie(
    val id: Long,
    val ageLimit: Int,
    val title: String,
    val genres: List<Genre>,
    val reviewCount: Long,
    val isLiked: Boolean,
    val rating: Int,
    val imageUrl: String?,
    val popularity: Double
)

fun Movie.mapToMovieEntity(): MovieEntity =
    MovieEntity(
        id = this.id,
        pgAge = this.ageLimit,
        title = this.title,
        reviewCount = this.reviewCount,
        isLiked = this.isLiked,
        rating = this.rating,
        imageUrl = this.imageUrl,
        popularity = this.popularity,
        storyLine = ""
    )
