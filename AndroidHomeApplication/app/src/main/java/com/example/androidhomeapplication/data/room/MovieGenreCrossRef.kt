package com.example.androidhomeapplication.data.room

import androidx.room.Entity

@Entity(
    tableName = "MovieGenreCrossRef",
    primaryKeys = ["movieId", "genreId"]
)
data class MovieGenreCrossRef(
    val movieId: Long,
    val genreId: Long
)
