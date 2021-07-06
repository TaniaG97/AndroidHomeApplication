package com.example.androidhomeapplication.data.room

import androidx.room.Embedded
import androidx.room.Relation
import com.example.androidhomeapplication.data.room.ActorEntity
import com.example.androidhomeapplication.data.room.GenreEntity
import com.example.androidhomeapplication.data.room.MovieEntity

data class MovieWithGenresAndActors(
    @Embedded
    val movie: MovieEntity,
    @Relation(parentColumn = "id", entityColumn = "parentId")
    val genres: List<GenreEntity>,
    @Relation(parentColumn = "id", entityColumn = "parentId")
    val actors: List<ActorEntity>
)