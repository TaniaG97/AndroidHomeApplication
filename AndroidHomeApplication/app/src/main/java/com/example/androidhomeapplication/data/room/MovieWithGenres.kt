package com.example.androidhomeapplication.data.room

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.androidhomeapplication.data.models.Movie

data class MovieWithGenres(
    @Embedded
    val movie: MovieEntity,

    @Relation(
        parentColumn = MovieEntity.COL_MOVIE_ID,
        entityColumn = MovieDetailsEntity.COL_MOVIE_ID
    )
    val movieDetails: MovieDetailsEntity?,

    @Relation(
        parentColumn = MovieEntity.COL_MOVIE_ID,
        entityColumn = GenreEntity.COL_GENRE_ID,
        associateBy = Junction(MovieGenreCrossRef::class)
    )
    val genres: List<GenreEntity>
)

fun MovieWithGenres.mapToMovie(): Movie =
    Movie(
        id = this.movie.movieId,
        ageLimit = this.movie.pgAge,
        title = this.movie.title,
        genres = this.genres.map { genreEntity -> genreEntity.mapToGenre()},
        reviewCount = this.movie.reviewCount,
        isLiked = this.movie.isLiked,
        rating = this.movie.rating,
        imageUrl = this.movie.imageUrl,
        popularity = this.movie.popularity
    )
