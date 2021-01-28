package com.example.androidhomeapplication.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.androidhomeapplication.R
import com.example.androidhomeapplication.navigation.BackButtonListener
import com.example.androidhomeapplication.navigation.RouterProvider
import com.example.androidhomeapplication.navigation.screens.FragmentScreen
import kotlinx.android.synthetic.main.fragment_movies_list.*

class FragmentMoviesList : Fragment(R.layout.fragment_movies_list), BackButtonListener {

    companion object {
        fun newInstance() = FragmentMoviesList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movie_list_item.setOnClickListener {
            val screen = FragmentScreen(FragmentMovieDetails.newInstance())
            (parentFragment as RouterProvider).router.navigateTo(screen)
        }
    }

    override fun onBackPressed(): Boolean {
        (parentFragment as RouterProvider).router.exit()
        return true
    }
}