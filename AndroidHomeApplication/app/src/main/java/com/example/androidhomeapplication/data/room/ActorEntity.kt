package com.example.androidhomeapplication.data.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "actor",
    foreignKeys = [
        ForeignKey(
            entity = MovieEntity::class,
            parentColumns = ["id"],
            childColumns = ["parentId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ActorEntity(
    @PrimaryKey(autoGenerate = true)
    val primaryKey: Long = 0,
    val parentId: Long,
    val id: Long,
    val name: String,
    val imageUrl: String?
)