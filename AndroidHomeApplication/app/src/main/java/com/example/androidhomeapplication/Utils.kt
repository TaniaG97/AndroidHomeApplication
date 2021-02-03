package com.example.androidhomeapplication

import android.content.res.ColorStateList
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat

object Utils {

    fun setImageActiveState(isActive:Boolean, imageView:ImageView, activeColorId:Int = R.color.radical_red, passiveColorId:Int = R.color.storm_gray){
        imageView.imageTintList = if (isActive) {
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    imageView.context,
                    activeColorId
                )
            )
        } else {
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    imageView.context,
                    passiveColorId
                )
            )
        }
    }

}