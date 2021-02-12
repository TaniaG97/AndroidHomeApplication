package com.example.androidhomeapplication.data

import com.example.androidhomeapplication.models.Genre
import kotlinx.serialization.Serializable

@Serializable
internal class JsonGenre(val id: Long, val name: String)

internal fun JsonGenre.mapToGenre(): Genre = Genre(
    id = this.id,
    name = this.name
)