package com.example.androidhomeapplication.data.models

import com.example.androidhomeapplication.data.db.MovieDbEntity

data class Movie(
    val id: Long,
    val ageLimit: Int,
    val title: String,
    val genres: List<Genre>,
    val reviewCount: Long,
    val isLiked: Boolean,
    val rating: Int,
    val imageUrl: String?
)

fun Movie.mapToMovieDbEntity() =
    MovieDbEntity(
        id = this.id,
        title = this.title,
        imageUrl = this.imageUrl ?: "",
        rating = this.rating,
        reviewCount = this.reviewCount,
        ageLimit = this.ageLimit,
        isLiked = this.isLiked,
        genres = this.genres,
        actors = listOf(),
        storyLine = ""
    )
