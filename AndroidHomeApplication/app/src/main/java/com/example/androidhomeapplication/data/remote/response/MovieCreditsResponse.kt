package com.example.androidhomeapplication.data.remote.response

import com.example.androidhomeapplication.data.models.Actor
import kotlinx.serialization.*

@Serializable
data class MovieCreditsResponse (
    @SerialName("id")
    val id: Long,

    @SerialName("cast")
    val cast: List<CastResponse>,

    @SerialName("crew")
    val crew: List<CastResponse>
)

@Serializable
data class CastResponse (
    @SerialName("adult")
    val adult: Boolean,

    @SerialName("gender")
    val gender: Long,

    @SerialName("id")
    val id: Long,

    @SerialName("known_for_department")
    val knownForDepartment: String,

    @SerialName("name")
    val name: String,

    @SerialName("original_name")
    val originalName: String,

    @SerialName("popularity")
    val popularity: Double,

    @SerialName("profile_path")
    val profilePath: String? = null,

    @SerialName("cast_id")
    val castID: Long? = null,

    @SerialName("character")
    val character: String? = null,

    @SerialName("credit_id")
    val creditID: String,

    @SerialName("order")
    val order: Long? = null,
)

fun CastResponse.mapToActor(profileURL: String): Actor =
    Actor(
        id = this.id,
        name = this.name,
        imageUrl = profileURL + this.profilePath
    )