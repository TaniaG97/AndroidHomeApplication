package com.example.androidhomeapplication.data.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.androidhomeapplication.data.models.Actor
import com.example.androidhomeapplication.data.room.ActorEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class ActorEntity(
    @PrimaryKey
    val actorId: Long,
    val name: String,
    val imageUrl: String?
) {
    companion object {
        const val TABLE_NAME = "actor"
        const val COL_ACTOR_ID = "actorId"
        const val COL_NAME = "name"
        const val COL_IMAGE_URL = "imageUrl"
    }
}

fun ActorEntity.mapToActor(): Actor =
    Actor(
        id = this.actorId,
        name = this.name,
        imageUrl = this.imageUrl
    )