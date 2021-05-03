package com.example.androidhomeapplication.data.db

import com.example.androidhomeapplication.data.repository.MoviesRepository

interface DbProvider {
    val db: FilmDatabase
}