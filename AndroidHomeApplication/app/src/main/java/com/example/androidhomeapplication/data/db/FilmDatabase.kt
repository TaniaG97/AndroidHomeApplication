package com.example.androidhomeapplication.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MovieDbEntity::class], version = 1, exportSchema = false)
abstract class FilmDatabase : RoomDatabase() {

    abstract fun filmDao(): FilmDao

    companion object {
        private const val DB_NAME = "movie_database"

        fun getDatabase(context: Context): FilmDatabase =
            Room.databaseBuilder(
                context.applicationContext,
                FilmDatabase::class.java,
                DB_NAME
            ).build()
    }
}