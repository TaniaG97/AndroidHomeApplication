package com.example.androidhomeapplication.data.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.androidhomeapplication.data.models.Genre
import com.example.androidhomeapplication.data.models.mapToGenreEntity

@Entity(
    tableName = "genre",
    foreignKeys = [
        ForeignKey(
            entity = MovieEntity::class,
            parentColumns = ["id"],
            childColumns = ["parentId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class GenreEntity(
    @PrimaryKey(autoGenerate = true)
    val primaryKey: Long = 0,
    val parentId: Long,
    val id: Long,
    val name: String
)

fun GenreEntity.mapToGenre(): Genre =
    Genre(
        id = this.id,
        name = this.name
    )