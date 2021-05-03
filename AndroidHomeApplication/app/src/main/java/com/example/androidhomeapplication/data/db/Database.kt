package com.example.androidhomeapplication.data.db

import android.content.Context
import androidx.room.Room

object Database {

    private var db: FilmDatabase? = null

    fun getInstance(context: Context): FilmDatabase {
        return db ?: Room.databaseBuilder(
            context.applicationContext,
            FilmDatabase::class.java,
            DB_NAME)
            .fallbackToDestructiveMigration()
            .build().also {
                db = it
            }
    }
}

private const val DB_NAME = "movieDatabase"