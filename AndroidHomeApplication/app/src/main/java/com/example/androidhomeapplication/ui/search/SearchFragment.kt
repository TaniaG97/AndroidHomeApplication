package com.example.androidhomeapplication.ui.search

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.androidhomeapplication.R
import com.example.androidhomeapplication.databinding.FragmentSearchBinding
import com.example.androidhomeapplication.movieRepository
import com.example.androidhomeapplication.navigation.RouterProvider
import com.example.androidhomeapplication.ui.movieDetails.MovieDetailsScreen
import com.example.androidhomeapplication.ui.moviesList.MoviesListAdapter
import com.github.terrakok.cicerone.androidx.FragmentScreen
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchFragment : Fragment(R.layout.fragment_search) {
    private val binding by viewBinding(FragmentSearchBinding::bind)
    private val viewModel: SearchViewModel by viewModels {
        SearchViewModelFactory(
            movieRepository
        )
    }

    private val pagingAdapter: MoviesListAdapter = MoviesListAdapter(
        onItemClick = { item ->
            (activity?.application as? RouterProvider)?.router?.navigateTo(MovieDetailsScreen(item.id))
        })

    private var searchJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()

        binding.searchRv.layoutManager = GridLayoutManager(context, 2)
        binding.searchRv.adapter = pagingAdapter

    }

    private fun initListeners() {
        binding.searchView.searchViewIcon.setOnClickListener {
            search(binding.searchView.searchViewEditText.text.toString())
        }
        binding.arrowBackIcon.setOnClickListener {
            (activity?.application as? RouterProvider)?.router?.exit()
        }
    }

    private fun search(query: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchMovies(query).collectLatest {
                pagingAdapter.submitData(it)
            }
        }
    }
}

class SearchScreen : FragmentScreen(
    key = "SearchScreen",
    fragmentCreator = { fragmentFactory: FragmentFactory -> SearchFragment() }
)