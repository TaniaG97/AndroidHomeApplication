package com.example.androidhomeapplication.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity( tableName = "movie")
data class MovieEntity(
    @PrimaryKey
    val id: Long,
    val pgAge: Int,
    val title: String,
    val reviewCount: Long,
    val isLiked: Boolean,
    val rating: Int,
    val imageUrl: String?,
    val popularity: Double,
    val storyLine: String
)