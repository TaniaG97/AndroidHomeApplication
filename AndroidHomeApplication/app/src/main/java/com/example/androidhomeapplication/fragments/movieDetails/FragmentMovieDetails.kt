package com.example.androidhomeapplication.fragments.movieDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidhomeapplication.R
import com.example.androidhomeapplication.Utils
import com.example.androidhomeapplication.Utils.setImageActiveState
import com.example.androidhomeapplication.databinding.FragmentMovieDetailsBinding
import com.example.androidhomeapplication.models.CastData
import com.example.androidhomeapplication.models.MovieData
import com.example.androidhomeapplication.navigation.RouterProvider
import com.github.terrakok.cicerone.androidx.FragmentScreen

class FragmentMovieDetails : Fragment(R.layout.fragment_movie_details) {
    private lateinit var binding: FragmentMovieDetailsBinding
    lateinit var adapter: CastsListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMovieDetailsBinding.bind(view)

        val stars: List<ImageView> = listOf(
            view.findViewById(R.id.star_1),
            view.findViewById(R.id.star_2),
            view.findViewById(R.id.star_3),
            view.findViewById(R.id.star_4),
            view.findViewById(R.id.star_5)
        )

        initViews(stars)

        binding.textBack.setOnClickListener {
            (activity?.application as? RouterProvider)?.router?.exit()
        }
    }

    private fun initViews(stars: List<ImageView>){
        val movieData = arguments?.getParcelable<MovieData>(KEY_MOVIE_DATA)

        movieData?.let {
            binding.backgroundImage.setImageResource(movieData.detailPoster)
            binding.textAge.text = context?.getString(R.string.age_template, movieData.ageLimit)
            binding.textTitle.text = movieData.title
            binding.textMoveTypes.text = movieData.genresList.joinToString(", ")

            stars.forEachIndexed { index, imageView ->
                if (index < movieData.starCount) {
                    imageView.setImageActiveState(isActive = true)
                } else {
                    imageView.setImageActiveState(isActive = false)
                }
            }

            binding.textReviews.text = getString(R.string.reviews_template, movieData.reviewCount)
            binding.textStorylineDescription.text = movieData.description

            adapter=CastsListAdapter()
            binding.castsRv.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false)
            binding.castsRv.adapter = adapter
            updateAdapter(movieData.castList)
        }
    }

    fun updateAdapter(castsList: List<CastData>) {
        adapter.submitList(castsList)
    }

    companion object {
        private const val KEY_MOVIE_DATA = "movie_data"

        fun getNewInstance(movieData: MovieData) = FragmentMovieDetails().apply {
            arguments = Bundle().apply {
                putParcelable(KEY_MOVIE_DATA, movieData)
            }
        }
    }
}

class MovieDetailsScreen(movieData: MovieData) : FragmentScreen(
    key = "MovieDetailsScreen",
    fragmentCreator = { fragmentFactory: FragmentFactory -> FragmentMovieDetails.getNewInstance(movieData) }
)