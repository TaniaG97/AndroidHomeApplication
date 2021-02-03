package com.example.androidhomeapplication.models

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CastData (
    val id: Long,
    val name: String,
    @DrawableRes val image: Int,
): Parcelable