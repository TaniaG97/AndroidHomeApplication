package com.example.androidhomeapplication.data.remote.response

import com.example.androidhomeapplication.data.models.Actor
import com.example.androidhomeapplication.data.models.Genre
import com.example.androidhomeapplication.data.models.MovieDetails
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenresResponse(
    @SerialName("genres")
    val genres: List<GenreResponse>
)

@Serializable
data class GenreResponse (
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String
)

fun GenreResponse.mapToGenre(): Genre =
    Genre(
        id = this.id,
        name = this.name
    )
