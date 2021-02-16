package com.example.androidhomeapplication.fragments.moviesList

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.academy.fundamentals.homework.data.MovieRepository
import com.android.academy.fundamentals.homework.data.MovieRepositoryProvider
import com.example.androidhomeapplication.R
import com.example.androidhomeapplication.databinding.FragmentMoviesListBinding
import com.example.androidhomeapplication.fragments.movieDetails.MovieDetailsScreen
import com.example.androidhomeapplication.getMovieRepository
import com.example.androidhomeapplication.navigation.RouterProvider
import com.github.terrakok.cicerone.androidx.FragmentScreen
import kotlinx.coroutines.*

class FragmentMoviesList : Fragment(R.layout.fragment_movies_list) {
    private val binding by viewBinding(FragmentMoviesListBinding::bind)
    private var scope: CoroutineScope? = null
    private val movieRepository: MovieRepository get() = this.getMovieRepository()

    private val adapter: MoviesListAdapter = MoviesListAdapter(
        onItemClick = { item ->
            (activity?.application as? RouterProvider)?.router?.navigateTo(MovieDetailsScreen(item.id))
        })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scope = MainScope()

        binding.cinemaRv.layoutManager = GridLayoutManager(context, 2)
        binding.cinemaRv.adapter = adapter
        showMoviesList()
    }

    override fun onDestroyView() {
        scope?.cancel()
        scope = null
        super.onDestroyView()
    }

    private fun showMoviesList() {
        scope?.launch {
            try {
                val moviesList = movieRepository.getMovies()
                adapter.submitList(moviesList)
            } catch (throwable: Throwable) {
                Toast.makeText(
                    requireContext(),
                    "Something was wrong. Look at the logs",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("FragmentMoviesList", "updateAdapter: Failed", throwable)
            }
        }
    }
}

class MoviesListScreen : FragmentScreen(
    key = "MoviesListScreen",
    fragmentCreator = { fragmentFactory: FragmentFactory -> FragmentMoviesList() }
)