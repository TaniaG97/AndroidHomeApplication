package com.example.androidhomeapplication.data.models

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Actor(
    val id: Long,
    val name: String,
    val imageUrl: String,
) : Parcelable