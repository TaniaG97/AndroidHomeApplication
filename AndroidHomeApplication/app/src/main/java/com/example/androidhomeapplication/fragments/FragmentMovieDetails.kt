package com.example.androidhomeapplication.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.example.androidhomeapplication.R
import com.example.androidhomeapplication.navigation.BackButtonListener
import com.example.androidhomeapplication.navigation.RouterProvider
import com.github.terrakok.cicerone.androidx.FragmentScreen
import kotlinx.android.synthetic.main.fragment_movie_details.*
import kotlinx.android.synthetic.main.fragment_movies_list.*

class FragmentMovieDetails : Fragment(R.layout.fragment_movie_details) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        text_back.setOnClickListener {
            (activity?.application as? RouterProvider)?.router?.exit()
        }
    }
}

class MovieDetailsScreen : FragmentScreen(
    key = "MovieDetailsScreen",
    fragmentCreator = { fragmentFactory: FragmentFactory -> FragmentMovieDetails() }
)