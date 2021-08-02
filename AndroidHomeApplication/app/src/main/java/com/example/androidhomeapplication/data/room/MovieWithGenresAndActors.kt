package com.example.androidhomeapplication.data.room

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.androidhomeapplication.data.models.Actor
import com.example.androidhomeapplication.data.models.Genre
import com.example.androidhomeapplication.data.models.Movie
import com.example.androidhomeapplication.data.models.MovieDetails
import com.example.androidhomeapplication.data.remote.response.MovieDetailsResponse
import com.example.androidhomeapplication.data.remote.response.mapToGenre
import com.example.androidhomeapplication.data.room.ActorEntity
import com.example.androidhomeapplication.data.room.GenreEntity
import com.example.androidhomeapplication.data.room.MovieEntity
import com.example.androidhomeapplication.utils.Utils

data class MovieWithGenresAndActors(
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
    val genres: List<GenreEntity>,

    @Relation(
        parentColumn = MovieEntity.COL_MOVIE_ID,
        entityColumn = ActorEntity.COL_ACTOR_ID,
        associateBy = Junction(MovieActorsCrossRef::class)
    )
    val actors: List<ActorEntity>
)

fun MovieWithGenresAndActors.mapToMovieDetails(): MovieDetails =
    MovieDetails(
        movieBaseInfo = Movie(
            id =this.movie.movieId,
            ageLimit = this.movie.pgAge,
            title = this.movie.title,
            genres =this.genres.map { genreEntity -> genreEntity.mapToGenre() },
            reviewCount =this.movie.reviewCount,
            isLiked =this.movie.isLiked,
            rating =this.movie.rating,
            imageUrl =this.movie.imageUrl,
            popularity =this.movie.popularity
        ),
        storyLine = this.movieDetails?.storyLine ?: "",
        actors = this.actors.map { actorEntity -> actorEntity.mapToActor() }
    )