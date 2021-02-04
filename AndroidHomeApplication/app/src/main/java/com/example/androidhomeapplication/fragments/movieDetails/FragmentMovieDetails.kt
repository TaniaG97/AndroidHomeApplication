package com.example.androidhomeapplication.fragments.movieDetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.androidhomeapplication.DataGenerator
import com.example.androidhomeapplication.R
import com.example.androidhomeapplication.databinding.FragmentMovieDetailsBinding
import com.example.androidhomeapplication.models.CastData
import com.example.androidhomeapplication.models.MovieData
import com.example.androidhomeapplication.navigation.RouterProvider
import com.example.androidhomeapplication.setRating
import com.github.terrakok.cicerone.androidx.FragmentScreen

private const val KEY_MOVIE_ID = "movie_id"

class FragmentMovieDetails : Fragment(R.layout.fragment_movie_details) {
    private val binding by viewBinding(FragmentMovieDetailsBinding::bind)
    private val adapter: CastsListAdapter = CastsListAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        val movieId = arguments?.getLong(KEY_MOVIE_ID)
        val movieData = DataGenerator.getMoviesList().find { it.id == movieId }
        if (movieData != null) {
            setMovieFields(movieData)
        }
    }

    private fun initViews() {
        binding.castsRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.castsRv.adapter = adapter

        binding.textBack.setOnClickListener {
            (activity?.application as? RouterProvider)?.router?.exit()
        }
    }

    private fun setMovieFields(movieData: MovieData) {
        binding.backgroundImage.setImageResource(movieData.detailPosterResId)
        binding.textAge.text = context?.getString(R.string.age_template, movieData.ageLimit)
        binding.textTitle.text = movieData.title
        binding.textMoveTypes.text = movieData.genresList.joinToString(", ")
        binding.stars.setRating(movieData.starCount)
        binding.textReviews.text = getString(R.string.reviews_template, movieData.reviewCount)
        binding.textStorylineDescription.text = movieData.description
        updateAdapter(movieData.castList)
    }

    private fun updateAdapter(castsList: List<CastData>) {
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


