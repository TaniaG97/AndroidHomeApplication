package com.example.androidhomeapplication.data.models

import com.example.androidhomeapplication.data.room.ActorEntity

data class Actor(
    val id: Long,
    val name: String,
    val imageUrl: String?,
)

fun Actor.mapToActorEntity(): ActorEntity =
    ActorEntity(
        actorId = this.id,
        name = this.name,
        imageUrl = this.imageUrl
    )