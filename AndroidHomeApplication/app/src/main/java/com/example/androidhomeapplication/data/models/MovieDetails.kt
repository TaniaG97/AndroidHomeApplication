package com.example.androidhomeapplication.data.models

data class MovieDetails(
    val id: Long,
    val ageLimit: Int,
    val title: String,
    val genres: List<Genre>,
    val reviewCount: Long,
    val isLiked: Boolean,
    val rating: Int,
    val detailImageUrl: String,
    val storyLine: String,
    val actors: List<Actor>
)