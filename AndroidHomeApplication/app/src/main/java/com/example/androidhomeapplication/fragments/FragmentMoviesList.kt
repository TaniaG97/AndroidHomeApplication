package com.example.androidhomeapplication.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.example.androidhomeapplication.R
import com.example.androidhomeapplication.navigation.BackButtonListener
import com.example.androidhomeapplication.navigation.RouterProvider
import com.github.terrakok.cicerone.androidx.FragmentScreen
import kotlinx.android.synthetic.main.fragment_movies_list.*

class FragmentMoviesList : Fragment(R.layout.fragment_movies_list) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movie_list_item.setOnClickListener {
            (activity?.application as? RouterProvider)?.router?.navigateTo(MovieDetailsScreen())
        }
    }
}

class MoviesListScreen : FragmentScreen(
    key = "MoviesListScreen",
    fragmentCreator = { fragmentFactory: FragmentFactory -> FragmentMoviesList() }
)