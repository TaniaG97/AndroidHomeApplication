package com.example.androidhomeapplication.data.room

import androidx.room.Entity

@Entity(
    tableName = "MovieActorsCrossRef",
    primaryKeys = ["movieId", "actorId"]
)
data class MovieActorsCrossRef(
    val movieId: Long,
    val actorId: Long
)
