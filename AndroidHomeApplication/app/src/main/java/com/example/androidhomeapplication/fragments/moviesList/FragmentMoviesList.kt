package com.example.androidhomeapplication.fragments.moviesList

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.androidhomeapplication.DataGenerator
import com.example.androidhomeapplication.R
import com.example.androidhomeapplication.databinding.FragmentMovieDetailsBinding
import com.example.androidhomeapplication.databinding.FragmentMoviesListBinding
import com.example.androidhomeapplication.fragments.movieDetails.CastsListAdapter
import com.example.androidhomeapplication.fragments.movieDetails.MovieDetailsScreen
import com.example.androidhomeapplication.models.CastData
import com.example.androidhomeapplication.models.MovieData
import com.example.androidhomeapplication.navigation.RouterProvider
import com.github.terrakok.cicerone.androidx.FragmentScreen

class FragmentMoviesList : Fragment(R.layout.fragment_movies_list) {
    private val binding by viewBinding(FragmentMoviesListBinding::bind)
    lateinit var adapter: MoviesListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        adapter = MoviesListAdapter(
            onItemClick = { item->
                (activity?.application as? RouterProvider)?.router?.navigateTo(MovieDetailsScreen(item.id))
            })

        binding.cinemaRv.layoutManager = GridLayoutManager(context, 2)
        binding.cinemaRv.adapter = adapter
        updateAdapter(DataGenerator.getMoviesList())
    }

    private fun updateAdapter(moviesList: List<MovieData>) {
        adapter.submitList(moviesList)
    }
}

class MoviesListScreen : FragmentScreen(
    key = "MoviesListScreen",
    fragmentCreator = { fragmentFactory: FragmentFactory -> FragmentMoviesList() }
)
