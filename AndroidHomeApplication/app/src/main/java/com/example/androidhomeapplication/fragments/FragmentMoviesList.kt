package com.example.androidhomeapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.androidhomeapplication.App
import com.example.androidhomeapplication.R
import com.example.androidhomeapplication.navigation.screens.Screens
import kotlinx.android.synthetic.main.fragment_movies_list.*
import kotlinx.android.synthetic.main.fragment_movies_list.view.*
import kotlinx.android.synthetic.main.fragment_movies_list.view.movie_list_item
import ru.terrakok.cicerone.Router

class FragmentMoviesList : Fragment(R.layout.fragment_movies_list){
    lateinit var router: Router

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        router = App.INSTANCE?.getRouter()!!
        movie_list_item.setOnClickListener {
            router.navigateTo(Screens.MovieDetails())
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            FragmentMoviesList()
    }
}