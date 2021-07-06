package com.example.androidhomeapplication.data.room

import androidx.room.*
import com.example.androidhomeapplication.data.models.*
import kotlinx.coroutines.flow.Flow

const val TABLE_NAME = "movie"
const val COL_ID = "id"
const val COL_POPULARITY = "popularity"


@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMovieItem(movie: Movie) {
        insertMovieEntities(movie.mapToMovieEntity())
        insertGenreEntities(movie.genres.map { genre -> genre.mapToGenreEntity(movie.id) })
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMovieDetailsItem(movieDetails: MovieDetails) {
        insertMovieEntities(movieDetails.mapToMovieEntity())
        insertGenreEntities(movieDetails.movieBaseInfo.genres.map { genre ->
            genre.mapToGenreEntity(movieDetails.movieBaseInfo.id)
        })
        insertActorEntities(movieDetails.actors.map { actor ->
            actor.mapToActorEntity(movieDetails.movieBaseInfo.id)
        })
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<Movie>) {
        movies.forEach { movie ->
            saveMovieItem(movie)
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieEntities(movieEntity: MovieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenreEntities(genreEntity: List<GenreEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActorEntities(actorEntity: List<ActorEntity>)

    @Query("SELECT * FROM $TABLE_NAME WHERE $COL_ID=:movieId")
    suspend fun getMovieById(movieId: Long): MovieEntity?

    @Query("SELECT * FROM $TABLE_NAME WHERE $COL_ID=:movieId")
    suspend fun getMovieWithGenresById(movieId: Long): MovieWithGenres?

    @Query("SELECT * FROM $TABLE_NAME WHERE $COL_ID=:movieId")
    suspend fun getMovieWithGenresAndActorsById(movieId: Long): MovieWithGenresAndActors?

    @Query("SELECT * FROM $TABLE_NAME ORDER BY $COL_POPULARITY DESC")
    suspend fun getPopularMovies(): List<MovieWithGenres>

    @Query("SELECT * FROM $TABLE_NAME ORDER BY $COL_POPULARITY DESC")
    fun getPopularMoviesFlow(): Flow<List<MovieWithGenres>>

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun clearTable()

}