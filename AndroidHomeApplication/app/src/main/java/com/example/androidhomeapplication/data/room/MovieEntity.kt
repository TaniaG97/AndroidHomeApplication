package com.example.androidhomeapplication.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.androidhomeapplication.data.room.MovieEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class MovieEntity(
    @PrimaryKey
    val movieId: Long,
    val pgAge: Int,
    val title: String,
    val reviewCount: Long,
    val isLiked: Boolean,
    val rating: Int,
    val imageUrl: String?,
    val popularity: Double
) {
    companion object {
        const val TABLE_NAME = "movie"
        const val COL_MOVIE_ID = "movieId"
        const val COL_PAGE_AGE = "pgAge"
        const val COL_TITLE = "title"
        const val COL_REVIEW_COUNT = "reviewCount"
        const val COL_IS_LIKED = "isLiked"
        const val COL_RATING = "rating"
        const val COL_IMAGE_URL = "imageUrl"
        const val COL_POPULARITY = "popularity"
    }
}