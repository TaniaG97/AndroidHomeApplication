package com.example.androidhomeapplication.fragments.moviesList

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.recyclerview.widget.GridLayoutManager
import com.example.androidhomeapplication.DataGenerator
import com.example.androidhomeapplication.R
import com.example.androidhomeapplication.fragments.movieDetails.MovieDetailsScreen
import com.example.androidhomeapplication.models.MovieData
import com.example.androidhomeapplication.navigation.RouterProvider
import com.github.terrakok.cicerone.androidx.FragmentScreen
import kotlinx.android.synthetic.main.fragment_movies_list.*

class FragmentMoviesList : Fragment(R.layout.fragment_movies_list) {
    private var moviesData: MutableList<MovieData> = mutableListOf()
    private var moviesListAdapter: MoviesListAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cinema_rv.layoutManager = GridLayoutManager(context, 2)

        moviesListAdapter = MoviesListAdapter(
            items = DataGenerator.getMoviesList(),
            onItemClick = { item->
                (activity?.application as? RouterProvider)?.router?.navigateTo(MovieDetailsScreen(item))
            })
        cinema_rv.adapter = moviesListAdapter

    }
}

class MoviesListScreen : FragmentScreen(
    key = "MoviesListScreen",
    fragmentCreator = { fragmentFactory: FragmentFactory -> FragmentMoviesList() }
)
