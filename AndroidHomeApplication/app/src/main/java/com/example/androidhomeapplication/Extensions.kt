package com.example.androidhomeapplication

import android.content.Context
import android.content.res.ColorStateList
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.CheckResult
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.androidhomeapplication.data.repository.RepositoryProvider
import com.bumptech.glide.Glide
import com.example.androidhomeapplication.data.repository.MoviesRepository
import com.example.androidhomeapplication.databinding.ViewStarsBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

fun ImageView.setImageActiveState(
    isActive: Boolean,
    @ColorRes activeColorResId: Int = R.color.radical_red,
    @ColorRes passiveColorResId: Int = R.color.storm_gray
) {
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

fun ImageView.loadImageWithGlide(imageUrl: String?) {
    Glide.with(this.context)
        .load(imageUrl)
        .placeholder(R.drawable.background_rect_with_border)
        .into(this)
}

fun ViewStarsBinding.setRating(activeElements: Int) {
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

fun Context.readAssetFileToString(fileName: String): String {
    val stream = this.assets.open(fileName)
    return stream.bufferedReader().readText()
}

val Fragment.movieRepository: MoviesRepository get() = (activity?.application as RepositoryProvider).movieRepository

fun Fragment.showShortToast(msg: String) =
    Toast.makeText(
        requireContext(),
        msg,
        Toast.LENGTH_SHORT
    ).show()

fun Fragment.showShortToast(@StringRes msgResId: Int) = this.showShortToast(getString(msgResId))

fun EditText.textChanges(): Flow<CharSequence?> {
    return callbackFlow<CharSequence?> {
        val listener = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = Unit
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                offer(s)
            }
        }
        addTextChangedListener(listener)
        awaitClose { removeTextChangedListener(listener) }
    }.onStart { emit(text) }
}

//fun EditText.textChanges(): StateFlow<String> {
//
//    val query = MutableStateFlow("")
//
//    val listener = object : TextWatcher {
//        override fun afterTextChanged(s: Editable?) = Unit
//        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
//        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            query.value = s.toString()
//        }
//    }
//    addTextChangedListener(listener)
//
//    return query
//}