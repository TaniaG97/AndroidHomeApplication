package com.example.androidhomeapplication.data.room

import androidx.room.*
import com.example.androidhomeapplication.data.models.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieEntities(movieEntity: MovieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieDetailsEntities(movieEntity: MovieDetailsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenreEntities(genreEntity: List<GenreEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActorEntities(actorEntity: List<ActorEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieActorsCrossRef(movieActorsCrossRef: MovieActorsCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieGenreCrossRef(movieGenreCrossRef: MovieGenreCrossRef)

    @Transaction
    suspend fun saveMovieItem(movie: Movie) {
        movie.genres.forEach { genre ->
            insertMovieGenreCrossRef(MovieGenreCrossRef(movie.id, genre.id))
        }
        insertMovieEntities(movie.mapToMovieEntity())
        insertGenreEntities(movie.genres.map { genre -> genre.mapToGenreEntity() })
    }

    @Transaction
    suspend fun saveMovieDetailsItem(movieDetails: MovieDetails) {
        movieDetails.movieBaseInfo.genres.forEach { genre ->
            insertMovieGenreCrossRef(MovieGenreCrossRef(movieDetails.movieBaseInfo.id, genre.id))
        }
        movieDetails.actors.forEach { actor ->
            insertMovieActorsCrossRef(MovieActorsCrossRef(movieDetails.movieBaseInfo.id, actor.id))
        }
        insertMovieEntities(movieDetails.mapToMovieEntity())
        insertMovieDetailsEntities(
            MovieDetailsEntity(
                movieEntityId = movieDetails.movieBaseInfo.id,
                storyLine = movieDetails.storyLine
            )
        )
        insertGenreEntities(movieDetails.movieBaseInfo.genres.map { genre ->
            genre.mapToGenreEntity()
        })
        insertActorEntities(movieDetails.actors.map { actor ->
            actor.mapToActorEntity()
        })
    }

    @Transaction
    suspend fun insertMovies(movies: List<Movie>) {
        movies.forEach { movie ->
            saveMovieItem(movie)
        }
    }

    @Query("SELECT * FROM ${MovieEntity.TABLE_NAME} WHERE ${MovieEntity.COL_MOVIE_ID}=:movieId")
    suspend fun getMovieById(movieId: Long): MovieEntity?

    @Query("SELECT * FROM ${MovieEntity.TABLE_NAME} WHERE ${MovieEntity.COL_MOVIE_ID}=:movieId")
    suspend fun getMovieWithGenresById(movieId: Long): MovieWithGenres?

    @Query("SELECT * FROM ${MovieEntity.TABLE_NAME} WHERE ${MovieEntity.COL_MOVIE_ID}=:movieId")
    suspend fun getMovieWithGenresAndActorsById(movieId: Long): MovieWithGenresAndActors?

    @Query("SELECT * FROM ${MovieEntity.TABLE_NAME} ORDER BY ${MovieEntity.COL_POPULARITY} DESC")
    suspend fun getPopularMovies(): List<MovieWithGenres>

    @Query("SELECT * FROM ${MovieEntity.TABLE_NAME} ORDER BY ${MovieEntity.COL_POPULARITY} DESC")
    fun getPopularMoviesFlow(): Flow<List<MovieWithGenres>>

    @Query("DELETE FROM ${MovieEntity.TABLE_NAME}")
    suspend fun clearTable()
}