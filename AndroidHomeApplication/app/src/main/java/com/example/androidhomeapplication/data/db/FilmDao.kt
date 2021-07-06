package com.example.androidhomeapplication.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

const val TABLE_NAME = "movies"
const val COL_ID = "id"
const val COL_POPULARITY = "popularity"

@Dao
interface FilmDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMovieItem(movieDbEntity: MovieDbEntity)

    @Delete
    suspend fun deleteMovieItem(movieDbEntity: MovieDbEntity)

    @Query("SELECT * FROM $TABLE_NAME WHERE $COL_ID=:movieId")
    suspend fun getMovieById(movieId: Long): MovieDbEntity?

    @Query("SELECT * FROM $TABLE_NAME ORDER BY $COL_POPULARITY DESC")
    fun getPopularMoviesFlow(): Flow<List<MovieDbEntity>>

    @Query("SELECT * FROM $TABLE_NAME ORDER BY $COL_POPULARITY DESC")
    suspend fun getPopularMovies(): List<MovieDbEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movieDbEntities: List<MovieDbEntity>)

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun clearTable()
}