package com.example.androidhomeapplication.fragments.movieDetails

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidhomeapplication.DataGenerator
import com.example.androidhomeapplication.R
import com.example.androidhomeapplication.models.MovieData
import com.example.androidhomeapplication.navigation.RouterProvider
import com.github.terrakok.cicerone.androidx.FragmentScreen
import kotlinx.android.synthetic.main.fragment_movie_details.*
import kotlinx.android.synthetic.main.fragment_movies_list.*

class FragmentMovieDetails : Fragment(R.layout.fragment_movie_details) {
    private var castsListAdapter: CastsListAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        text_back.setOnClickListener {
            (activity?.application as? RouterProvider)?.router?.exit()
        }
    }

    private fun initViews(){
        val movieData = arguments?.getSerializable(KEY_MOVIE_DATA) as? MovieData

        val stars: List<ImageView> = listOf(
            star_1,
            star_2,
            star_3,
            star_4,
            star_5
        )

        movieData?.let {
            background_image.setImageResource(movieData.detailPoster)
            text_age.text = "${movieData.ageLimit}+"
            text_title.text = movieData.title
            text_move_types.text = movieData.genresList.joinToString(", ")
            stars.forEachIndexed { index, imageView ->
                imageView.imageTintList = if (index < movieData.starCount) {
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.radical_red
                        )
                    )
                } else {
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.storm_gray
                        )
                    )
                }
            }
            text_reviews.text = "${movieData.reviewCount} Reviews"
            text_storyline_description.text = movieData.description

            casts_rv.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false)
            castsListAdapter = CastsListAdapter(movieData.castList)
            casts_rv.adapter = castsListAdapter
        }
    }

    companion object {
        private const val KEY_MOVIE_DATA = "movie_data"

        fun getNewInstance(movieData: MovieData) = FragmentMovieDetails().apply {
            arguments = Bundle().apply {
                putSerializable(KEY_MOVIE_DATA, movieData)
            }
        }
    }
}

class MovieDetailsScreen(movieData: MovieData) : FragmentScreen(
    key = "MovieDetailsScreen",
    fragmentCreator = { fragmentFactory: FragmentFactory -> FragmentMovieDetails.getNewInstance(movieData) }
)