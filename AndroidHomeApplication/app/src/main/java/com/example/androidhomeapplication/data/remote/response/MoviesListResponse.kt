package com.example.androidhomeapplication.data.remote.response

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MoviesListResponse(
    @Json(name = "page")
    val page: Long,

    @Json(name = "results")
    val results: List<MovieResponse>,

    @Json(name = "total_results")
    val totalResults: Long,

    @Json(name = "total_pages")
    val totalPages: Long
) : Parcelable