package com.example.androidhomeapplication.fragments.moviesList

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.androidhomeapplication.DataResult
import com.example.androidhomeapplication.R
import com.example.androidhomeapplication.databinding.FragmentMoviesListBinding
import com.example.androidhomeapplication.fragments.movieDetails.MovieDetailsScreen
import com.example.androidhomeapplication.models.Movie
import com.example.androidhomeapplication.movieRepository
import com.example.androidhomeapplication.navigation.RouterProvider
import com.github.terrakok.cicerone.androidx.FragmentScreen

class FragmentMoviesList : Fragment(R.layout.fragment_movies_list) {
    private val binding by viewBinding(FragmentMoviesListBinding::bind)
    private val viewModel: MoviesListViewModel by viewModels {
        MoviesListViewModelFactory(
            movieRepository
        )
    }

    private val adapter: MoviesListAdapter = MoviesListAdapter(
        onItemClick = { item ->
            (activity?.application as? RouterProvider)?.router?.navigateTo(MovieDetailsScreen(item.id))
        })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()

        binding.cinemaRv.layoutManager = GridLayoutManager(context, 2)
        binding.cinemaRv.adapter = adapter
        viewModel.getMoviesList()
    }

    private fun setupObservers() {

        viewModel.moviesList.observe(
            viewLifecycleOwner,
            {result: DataResult<List<Movie>> ->
                when (result) {
                    is DataResult.Default -> {
                    }
                    is DataResult.Loading -> {
                    }
                    is DataResult.Success<List<Movie>> -> {
                        adapter.submitList(result.value)
                    }
                    is DataResult.EmptyResult -> {
                        Toast.makeText(
                            requireContext(),
                            "Movies list is empty",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is DataResult.Error -> {
                        Log.e("FragmentMoviesList", "getMoviesList: Failed", result.error)
                        Toast.makeText(
                            requireContext(),
                            "Something was wrong. Look at the logs",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        )
    }
}

class MoviesListScreen : FragmentScreen(
    key = "MoviesListScreen",
    fragmentCreator = { fragmentFactory: FragmentFactory -> FragmentMoviesList() }
)