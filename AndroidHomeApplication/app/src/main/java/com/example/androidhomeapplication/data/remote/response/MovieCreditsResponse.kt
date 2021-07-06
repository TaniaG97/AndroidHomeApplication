package com.example.androidhomeapplication.data.remote.response

import android.os.Parcelable
import com.example.androidhomeapplication.data.models.Actor
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieCreditsResponse(
    @Json(name = "id")
    val id: Long,

    @Json(name = "cast")
    val cast: List<CastResponse>,

    @Json(name = "crew")
    val crew: List<CastResponse>
) : Parcelable

@Parcelize
data class CastResponse(
    @Json(name = "adult")
    val adult: Boolean,

    @Json(name = "gender")
    val gender: Long,

    @Json(name = "id")
    val id: Long,

    @Json(name = "known_for_department")
    val knownForDepartment: String,

    @Json(name = "name")
    val name: String,

    @Json(name = "original_name")
    val originalName: String,

    @Json(name = "popularity")
    val popularity: Double,

    @Json(name = "profile_path")
    val profilePath: String? = null,

    @Json(name = "cast_id")
    val castId: Long? = null,

    @Json(name = "character")
    val character: String? = null,

    @Json(name = "credit_id")
    val creditId: String,

    @Json(name = "order")
    val order: Long? = null,
) : Parcelable

fun CastResponse.mapToActor(profileUrl: String): Actor =
    Actor(
        id = this.id,
        name = this.name,
        imageUrl = profileUrl + this.profilePath
    )