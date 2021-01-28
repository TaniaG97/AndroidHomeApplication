package com.example.androidhomeapplication.navigation.container

import android.os.Bundle
import com.example.androidhomeapplication.fragments.FragmentMoviesList
import com.example.androidhomeapplication.navigation.screens.FragmentScreen

class MovieContainer : BaseFragmentContainer() {

    companion object {
        const val TAG = "MovieContainerTag"
        fun newInstance() = MovieContainer()
    }

    override fun getInitialFragmentScreen(params: Bundle?): FragmentScreen {
        return FragmentScreen(FragmentMoviesList.newInstance())
    }

}