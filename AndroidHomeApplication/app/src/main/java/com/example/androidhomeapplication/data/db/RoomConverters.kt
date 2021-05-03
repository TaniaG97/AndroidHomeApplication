package com.example.androidhomeapplication.data.db

import android.util.Log
import androidx.room.TypeConverter
import com.example.androidhomeapplication.data.models.Actor
import com.example.androidhomeapplication.data.models.Genre
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.IOException

object RoomConverters {

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @TypeConverter
    @JvmStatic
    fun genresToString(genres: List<Genre>?): String {
        val type = Types.newParameterizedType(List::class.java, Genre::class.java)
        val adapter = moshi.adapter<List<Genre>>(type)
        return adapter.toJson(genres)
    }

    @TypeConverter
    @JvmStatic
    fun stringToGenres(value: String): List<Genre> {
        val type = Types.newParameterizedType(List::class.java, Genre::class.java)
        val adapter = moshi.adapter<List<Genre>>(type)
        var result: List<Genre>? = null

        try {
            result = adapter.fromJson(value)
        } catch (exception: IOException) {
            Log.e("RoomConverters","Failed to parse genres: ${exception.localizedMessage}")
        }

        return result ?: listOf()
    }

    @TypeConverter
    @JvmStatic
    fun actorsToString(actors: List<Actor>?): String {
        val type = Types.newParameterizedType(List::class.java, Actor::class.java)
        val adapter = moshi.adapter<List<Actor>>(type)
        return adapter.toJson(actors)
    }

    @TypeConverter
    @JvmStatic
    fun stringToActors(value: String): List<Actor> {
        val type = Types.newParameterizedType(List::class.java, Actor::class.java)
        val adapter = moshi.adapter<List<Actor>>(type)
        var result: List<Actor>? = null

        try {
            result = adapter.fromJson(value)
        } catch (exception: IOException) {
            Log.e("RoomConverters","Failed to parse actors: ${exception.localizedMessage}")
        }

        return result ?: listOf()
    }
}