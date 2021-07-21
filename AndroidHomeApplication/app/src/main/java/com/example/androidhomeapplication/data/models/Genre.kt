package com.example.androidhomeapplication.data.models

import com.example.androidhomeapplication.data.room.GenreEntity

data class Genre(
    val id: Long,
    val name: String
)

fun Genre.mapToGenreEntity(): GenreEntity =
    GenreEntity(
        genreId = this.id,
        name = this.name
    )