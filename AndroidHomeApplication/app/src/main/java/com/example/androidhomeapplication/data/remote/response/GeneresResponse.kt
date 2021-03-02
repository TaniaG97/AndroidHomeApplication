package com.example.androidhomeapplication.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class GenresResponse(
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
