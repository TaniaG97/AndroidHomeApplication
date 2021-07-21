package com.example.androidhomeapplication.data.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.androidhomeapplication.data.models.Genre
import com.example.androidhomeapplication.data.models.mapToGenreEntity
import com.example.androidhomeapplication.data.room.GenreEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class GenreEntity(
    @PrimaryKey
    val genreId: Long,
    val name: String
) {
    companion object {
        const val TABLE_NAME = "genre"
        const val COL_GENRE_ID = "genreId"
        const val COL_NAME = "name"
    }
}

fun GenreEntity.mapToGenre(): Genre =
    Genre(
        id = this.genreId,
        name = this.name
    )