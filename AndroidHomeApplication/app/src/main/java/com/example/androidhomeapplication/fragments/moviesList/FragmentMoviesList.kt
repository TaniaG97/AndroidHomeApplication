package com.example.androidhomeapplication.fragments.moviesList

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.recyclerview.widget.GridLayoutManager
import com.example.androidhomeapplication.DataGenerator
import com.example.androidhomeapplication.R
import com.example.androidhomeapplication.databinding.FragmentMovieDetailsBinding
import com.example.androidhomeapplication.databinding.FragmentMoviesListBinding
import com.example.androidhomeapplication.fragments.movieDetails.MovieDetailsScreen
import com.example.androidhomeapplication.navigation.RouterProvider
import com.github.terrakok.cicerone.androidx.FragmentScreen

class FragmentMoviesList : Fragment(R.layout.fragment_movies_list) {
    private lateinit var binding: FragmentMoviesListBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMoviesListBinding.bind(view)

        binding.cinemaRv.layoutManager = GridLayoutManager(context, 2)

        val moviesListAdapter = MoviesListAdapter(
            items = DataGenerator.getMoviesList(),
            onItemClick = { item->
                (activity?.application as? RouterProvider)?.router?.navigateTo(MovieDetailsScreen(item))
            })
        binding.cinemaRv.adapter = moviesListAdapter

    }
}

class MoviesListScreen : FragmentScreen(
    key = "MoviesListScreen",
    fragmentCreator = { fragmentFactory: FragmentFactory -> FragmentMoviesList() }
)
