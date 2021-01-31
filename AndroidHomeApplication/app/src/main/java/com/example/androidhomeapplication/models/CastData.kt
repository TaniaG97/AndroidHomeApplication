package com.example.androidhomeapplication.models

import androidx.annotation.DrawableRes

data class CastData (
    val id: Int,
    val name: String,
    @DrawableRes val image: Int,
)