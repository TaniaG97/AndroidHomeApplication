package com.example.androidhomeapplication.data.models

import com.example.androidhomeapplication.data.room.MovieEntity

data class MovieDetails(
    val movieBaseInfo: Movie,
    val storyLine: String,
    val actors: List<Actor>
)

fun MovieDetails.mapToMovieEntity(): MovieEntity =
    MovieEntity(
        movieId = this.movieBaseInfo.id,
        pgAge = this.movieBaseInfo.ageLimit,
        title = this.movieBaseInfo.title,
        reviewCount = this.movieBaseInfo.reviewCount,
        isLiked = this.movieBaseInfo.isLiked,
        rating = this.movieBaseInfo.rating,
        imageUrl = this.movieBaseInfo.imageUrl,
        popularity = this.movieBaseInfo.popularity,
    )