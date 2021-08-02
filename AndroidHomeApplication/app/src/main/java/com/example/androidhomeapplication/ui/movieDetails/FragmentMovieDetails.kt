package com.example.androidhomeapplication.ui.movieDetails

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.androidhomeapplication.*
import com.example.androidhomeapplication.databinding.FragmentMovieDetailsBinding
import com.example.androidhomeapplication.data.models.Actor
import com.example.androidhomeapplication.data.models.MovieDetails
import com.example.androidhomeapplication.navigation.RouterProvider
import com.example.androidhomeapplication.utils.DataResult
import com.github.terrakok.cicerone.androidx.FragmentScreen

private const val KEY_MOVIE_ID = "movie_id"
private const val KEY_CACHE_RESULT = "cache_result"

class FragmentMovieDetails : Fragment(R.layout.fragment_movie_details) {
    private val binding by viewBinding(FragmentMovieDetailsBinding::bind)
    private val adapter: CastsListAdapter = CastsListAdapter()
    private val viewModel: MovieDetailsViewModel by viewModels {
        MovieDetailsViewModelFactory(
            movieRepository,
            arguments?.getLong(KEY_MOVIE_ID) ?: -1,
            arguments?.getBoolean(KEY_CACHE_RESULT) ?: false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        viewModel.movie.observe(viewLifecycleOwner, ::setResult)
    }

    private fun initViews() {
        binding.castsRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.castsRv.adapter = adapter

        binding.textBack.setOnClickListener {
            (activity?.application as? RouterProvider)?.router?.exit()
        }
    }

    private fun setResult(result: DataResult<MovieDetails>) =
        when (result) {
            is DataResult.Success<MovieDetails> -> {
                setMovieFields(result.value)
            }
            is DataResult.Error -> {
                showShortToast(result.error.message ?: getString(R.string.something_wrong))
            }
        }

    private fun setMovieFields(movieData: MovieDetails) {
        binding.backgroundImage.loadImageWithGlide(movieData.movieBaseInfo.imageUrl)
        binding.textAge.text =
            context?.getString(R.string.age_template, movieData.movieBaseInfo.ageLimit)
        binding.textTitle.text = movieData.movieBaseInfo.title
        binding.textMoveTypes.text =
            movieData.movieBaseInfo.genres.joinToString(", ") { genre -> genre.name }
        binding.stars.setRating(movieData.movieBaseInfo.rating)
        binding.textReviews.text =
            getString(R.string.reviews_template, movieData.movieBaseInfo.reviewCount)
        binding.textStorylineDescription.text = movieData.storyLine
        updateAdapter(movieData.actors)
    }

    private fun updateAdapter(castsList: List<Actor>) {
        adapter.submitList(castsList)
    }
}

class MovieDetailsScreen(private val movieId: Long, private val cacheResult: Boolean = false) :
    FragmentScreen(
        key = "MovieDetailsScreen",
        fragmentCreator = { fragmentFactory: FragmentFactory ->
            val fragment = FragmentMovieDetails()
            val args = Bundle(1)
            args.putLong(KEY_MOVIE_ID, movieId)
            args.putBoolean(KEY_CACHE_RESULT, cacheResult)
            fragment.arguments = args
            fragment
        }
    )