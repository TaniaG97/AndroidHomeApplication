package com.example.androidhomeapplication.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.androidhomeapplication.data.room.MovieDetailsEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class MovieDetailsEntity(
    @PrimaryKey
    val movieEntityId: Long,
    val storyLine: String
) {
    companion object {
        const val TABLE_NAME = "movie_details"
        const val COL_MOVIE_ID = "movieEntityId"
        const val COL_STORY_LINE = "storyLine"
    }
}
