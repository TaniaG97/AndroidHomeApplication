package com.example.androidhomeapplication.models

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieData(
    val id: Long,
    @DrawableRes val poster: Int,
    @DrawableRes val detailPoster: Int,
    val title: String,
    val description: String,
    val genresList: List<String>,
    val ageLimit: Int,
    val time: Int,
    val isLiked: Boolean,
    val starCount: Int,
    val reviewCount: Int,
    val castList: List<CastData>
): Parcelable

