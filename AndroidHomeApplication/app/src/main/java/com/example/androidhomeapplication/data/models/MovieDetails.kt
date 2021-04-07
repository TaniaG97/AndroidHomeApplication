package com.example.androidhomeapplication.data.models

data class MovieDetails(
    val movieBaseInfo: Movie,
    val storyLine: String,
    val actors: List<Actor>
)