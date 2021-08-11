package com.example.androidhomeapplication.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        MovieEntity::class,
        GenreEntity::class,
        ActorEntity::class,
        MovieGenreCrossRef::class,
        MovieActorsCrossRef::class,
        MovieDetailsEntity::class
    ],
    version = 1
)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    companion object {
        private const val DB_NAME = "movie_database"

        fun createDatabase(context: Context): MovieDatabase =
            Room.databaseBuilder(
                context.applicationContext,
                MovieDatabase::class.java,
                DB_NAME
            ).build()
    }
}