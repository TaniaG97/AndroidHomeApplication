package com.example.androidhomeapplication.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//@Database(entities = [MovieDbEntity::class], version = 1)
//abstract class FilmDatabase: RoomDatabase() {
//    abstract val filmDao: FilmDao
//}

@Database(entities = [MovieDbEntity::class], version = 1, exportSchema = false)
public abstract class FilmDatabase : RoomDatabase() {

    abstract fun filmDao(): FilmDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: FilmDatabase? = null

        fun getDatabase(context: Context): FilmDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FilmDatabase::class.java,
                    "movie_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}