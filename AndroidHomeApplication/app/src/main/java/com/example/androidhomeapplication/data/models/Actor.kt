package com.example.androidhomeapplication.data.models

import android.os.Parcelable
import androidx.room.Entity
import com.example.androidhomeapplication.data.room.ActorEntity
import kotlinx.android.parcel.Parcelize

data class Actor(
    val id: Long,
    val name: String,
    val imageUrl: String?,
)

fun Actor.mapToActorEntity(movieId: Long): ActorEntity =
    ActorEntity(
        parentId = movieId,
        id = this.id,
        name = this.name,
        imageUrl = this.imageUrl
    )