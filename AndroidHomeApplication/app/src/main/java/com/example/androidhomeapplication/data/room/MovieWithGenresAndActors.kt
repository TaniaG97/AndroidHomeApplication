package com.example.androidhomeapplication.data.room

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.androidhomeapplication.data.room.ActorEntity
import com.example.androidhomeapplication.data.room.GenreEntity
import com.example.androidhomeapplication.data.room.MovieEntity

data class MovieWithGenresAndActors(
    @Embedded
    val movie: MovieEntity,

    @Relation(
        parentColumn = MovieEntity.COL_MOVIE_ID,
        entityColumn = MovieDetailsEntity.COL_MOVIE_ID
    )
    val movieDetails: MovieDetailsEntity,

    @Relation(
        parentColumn = MovieEntity.COL_MOVIE_ID,
        entityColumn = GenreEntity.COL_GENRE_ID,
        associateBy = Junction(MovieGenreCrossRef::class)
    )
    val genres: List<GenreEntity>,

    @Relation(
        parentColumn = MovieEntity.COL_MOVIE_ID,
        entityColumn = ActorEntity.COL_ACTOR_ID,
        associateBy = Junction(MovieActorsCrossRef::class)
    )
    val actors: List<ActorEntity>
)