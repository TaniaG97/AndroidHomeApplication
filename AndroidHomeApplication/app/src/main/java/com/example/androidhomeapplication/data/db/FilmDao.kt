package com.example.androidhomeapplication.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FilmDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMovieItem(movieDbEntity: MovieDbEntity)

    @Delete
    suspend fun deleteMovieItem(movieDbEntity: MovieDbEntity)

    @Query("select * from movies WHERE id=:movieId")
    suspend fun getMovieById(movieId: Long): MovieDbEntity?

    @Query("select * from movies ")
    fun getMoviesFlow(): Flow<List<MovieDbEntity>>

    @Query("select * from movies ")
    suspend fun getMovies(): List<MovieDbEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movieDbEntities: List<MovieDbEntity>)

    @Query("DELETE FROM movies")
    suspend fun clearTable()
}