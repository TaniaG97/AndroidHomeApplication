package com.example.androidhomeapplication.data.room

import androidx.room.Embedded
import androidx.room.Relation
import com.example.androidhomeapplication.data.models.Genre
import com.example.androidhomeapplication.data.models.Movie

data class MovieWithGenres(
    @Embedded
    val movie: MovieEntity,
    @Relation(parentColumn = "id", entityColumn = "parentId")
    val genres: List<GenreEntity>
)

fun MovieWithGenres.mapToMovie(): Movie =
    Movie(
        id = this.movie.id,
        ageLimit = this.movie.pgAge,
        title = this.movie.title,
        genres = this.genres.map { genreEntity -> genreEntity.mapToGenre()},
        reviewCount = this.movie.reviewCount,
        isLiked = this.movie.isLiked,
        rating = this.movie.rating,
        imageUrl = this.movie.imageUrl,
        popularity = this.movie.popularity
    )
