package com.example.androidhomeapplication.data.local

import com.example.androidhomeapplication.data.models.Actor
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class JsonActor(
    val id: Long,
    val name: String,
    @SerialName("profile_path")
    val profilePicture: String
)

internal fun JsonActor.mapToActor(): Actor =
    Actor(
        id = this.id,
        name = this.name,
        imageUrl = this.profilePicture
    )