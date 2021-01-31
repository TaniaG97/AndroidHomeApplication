package com.example.androidhomeapplication.models

import androidx.annotation.DrawableRes
import java.io.Serializable

data class MovieData(
    val id: Int,
    @DrawableRes val poster: Int,
    @DrawableRes val detailPoster: Int,
    val title: String,
    val description: String,
    val genresList: List<String>,
    val ageLimit: Int,
    val time: Int,
    val isLiked: Boolean,
    val starCount: Int,
    val reviewCount: Int,
    val castList: List<CastData>
): Serializable

enum class MovieGenres(val value: String) {
    ACTION("Action"),
    ADVENTURE("Adventure"),
    DRAMA("Drama"),
    FANTASY("Fantasy"),
    SCI_FI("Sci-Fi"),
    THRILLER("Thriller")
}