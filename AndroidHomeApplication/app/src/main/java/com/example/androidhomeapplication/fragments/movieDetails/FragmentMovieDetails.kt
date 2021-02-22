package com.example.androidhomeapplication.fragments.movieDetails

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.academy.fundamentals.homework.data.MovieRepository
import com.android.academy.fundamentals.homework.data.MovieRepositoryProvider
import com.example.androidhomeapplication.*
import com.example.androidhomeapplication.databinding.FragmentMovieDetailsBinding
import com.example.androidhomeapplication.models.Actor
import com.example.androidhomeapplication.models.Movie
import com.example.androidhomeapplication.navigation.RouterProvider
import com.example.androidhomeapplication.observer.BaseDataObserver
import com.github.terrakok.cicerone.androidx.FragmentScreen
import kotlinx.coroutines.*

private const val KEY_MOVIE_ID = "movie_id"

class FragmentMovieDetails : Fragment(R.layout.fragment_movie_details) {
    private val binding by viewBinding(FragmentMovieDetailsBinding::bind)
    private val adapter: CastsListAdapter = CastsListAdapter()
    private val viewModel: MovieDetailsViewModel by viewModels { MovieDetailsViewModelFactory(movieRepository) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setupObservers()

        getMovieDetails()
    }

    private fun initViews() {
        binding.castsRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.castsRv.adapter = adapter

        binding.textBack.setOnClickListener {
            (activity?.application as? RouterProvider)?.router?.exit()
        }
    }

    private fun setupObservers() {

        viewModel.movie.observe(
            viewLifecycleOwner,
            object : BaseDataObserver<Movie>() {

                override fun onInProgress() {
                    Log.d("FragmentMovieDetails", "onInProgress")
                }

                override fun onData(data: Movie) {
                    Log.d("FragmentMovieDetails", "SUCCESS: ")
                    setMovieFields(data)
                }

                override fun onError() {
                    Log.d("FragmentMovieDetails", "ERROR")
                    Toast.makeText(
                        requireContext(),
                        "Something was wrong. Look at the logs",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }

    private fun getMovieDetails() {
        val movieId = arguments?.getLong(KEY_MOVIE_ID)
        if (movieId!=null){
            viewModel.getMovieDetails(movieId)
        }
    }

    private fun setMovieFields(movieData: Movie) {
        binding.backgroundImage.loadImageWithGlide(movieData.detailImageUrl)
        binding.textAge.text = context?.getString(R.string.age_template, movieData.pgAge)
        binding.textTitle.text = movieData.title
        binding.textMoveTypes.text = movieData.genres.joinToString(", ") { genre -> genre.name }
        binding.stars.setRating(movieData.rating)
        binding.textReviews.text = getString(R.string.reviews_template, movieData.reviewCount)
        binding.textStorylineDescription.text = movieData.storyLine
        updateAdapter(movieData.actors)
    }

    private fun updateAdapter(castsList: List<Actor>) {
        adapter.submitList(castsList)
    }
}

class MovieDetailsScreen(private val movieId: Long) : FragmentScreen(
    key = "MovieDetailsScreen",
    fragmentCreator = { fragmentFactory: FragmentFactory ->
        val fragment = FragmentMovieDetails()
        val args = Bundle(1)
        args.putLong(KEY_MOVIE_ID, movieId)
        fragment.arguments = args
        fragment
    }
)