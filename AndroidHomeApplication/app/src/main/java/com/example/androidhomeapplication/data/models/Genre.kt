package com.example.androidhomeapplication.data.models

import android.os.Parcelable
import androidx.room.Entity
import com.example.androidhomeapplication.data.room.GenreEntity
import kotlinx.android.parcel.Parcelize

data class Genre(
    val id: Long,
    val name: String
)

fun Genre.mapToGenreEntity(movieId: Long): GenreEntity =
    GenreEntity(
        parentId = movieId,
        id = this.id,
        name = this.name
    )