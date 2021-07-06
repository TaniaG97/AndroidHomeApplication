package com.example.androidhomeapplication.data.remote.response

import android.os.Parcelable
import com.example.androidhomeapplication.data.models.Actor
import com.example.androidhomeapplication.data.models.Genre
import com.example.androidhomeapplication.data.models.MovieDetails
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
data class GenresResponse(
    @Json(name = "genres")
    val genres: List<GenreResponse>
) : Parcelable

@Parcelize
data class GenreResponse(
    @Json(name = "id")
    val id: Long,
    @Json(name = "name")
    val name: String
) : Parcelable

fun GenreResponse.mapToGenre(): Genre =
    Genre(
        id = this.id,
        name = this.name
    )
