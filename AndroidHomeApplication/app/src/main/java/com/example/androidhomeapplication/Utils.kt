package com.example.androidhomeapplication

import android.content.res.ColorStateList
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.example.androidhomeapplication.databinding.ViewStarsBinding

fun ImageView.setImageActiveState(isActive:Boolean, @ColorRes activeColorResId:Int = R.color.radical_red, @ColorRes passiveColorResId:Int = R.color.storm_gray){
    @ColorRes val colorResId = if (isActive) {
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

fun ViewStarsBinding.setRating(activeElements: Int){
    arrayOf(
        this.star1,
        this.star2,
        this.star3,
        this.star4,
        this.star5
    ).forEachIndexed { index, imageView ->
        imageView.setImageActiveState(isActive = (index < activeElements))
    }
}