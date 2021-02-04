package com.example.androidhomeapplication.models

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.android.parcel.Parcelize

data class CastData (
    val id: Long,
    val name: String,
    @DrawableRes val imageResId: Int,
)