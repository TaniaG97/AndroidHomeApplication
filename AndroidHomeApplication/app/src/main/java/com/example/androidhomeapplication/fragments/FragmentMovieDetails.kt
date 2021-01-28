package com.example.androidhomeapplication.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.androidhomeapplication.R
import com.example.androidhomeapplication.navigation.BackButtonListener
import com.example.androidhomeapplication.navigation.RouterProvider
import kotlinx.android.synthetic.main.fragment_movie_details.*

class FragmentMovieDetails : Fragment(R.layout.fragment_movie_details), BackButtonListener {

    companion object {
        fun newInstance() = FragmentMovieDetails()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        text_back.setOnClickListener {
            (parentFragment as RouterProvider).router.exit()
        }
    }

    override fun onBackPressed(): Boolean {
        (parentFragment as RouterProvider).router.exit()
        return true
    }

}