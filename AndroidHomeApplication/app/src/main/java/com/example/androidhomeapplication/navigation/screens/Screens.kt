package com.example.androidhomeapplication.navigation.screens

import androidx.fragment.app.Fragment
import com.example.androidhomeapplication.fragments.FragmentMovieDetails
import com.example.androidhomeapplication.fragments.FragmentMoviesList
import ru.terrakok.cicerone.android.support.SupportAppScreen

class Screens {

    class MoviesList() :
        SupportAppScreen() {
        override fun getFragment(): Fragment? {
            return FragmentMoviesList.newInstance()
        }
    }
    class MovieDetails() :
        SupportAppScreen() {
        override fun getFragment(): Fragment? {
            return FragmentMovieDetails.newInstance()
        }
    }

}