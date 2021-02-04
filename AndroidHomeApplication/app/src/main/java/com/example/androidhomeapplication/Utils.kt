package com.example.androidhomeapplication

import android.content.res.ColorStateList
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

object Utils {

    fun ImageView.setImageActiveState(isActive:Boolean, @ColorRes activeColorResId:Int = R.color.radical_red, @ColorRes passiveColorResId:Int = R.color.storm_gray){
        val colorResId = if (isActive) {
            activeColorResId
        } else {
            passiveColorResId
        }

        this.imageTintList = ColorStateList.valueOf(
            ContextCompat.getColor(
                this.context,
                colorResId
            )
        )
    }

}