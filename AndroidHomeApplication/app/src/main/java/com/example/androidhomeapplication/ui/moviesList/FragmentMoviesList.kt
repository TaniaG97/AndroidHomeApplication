package com.example.androidhomeapplication.ui.moviesList

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.androidhomeapplication.R
import com.example.androidhomeapplication.databinding.FragmentMoviesListBinding
import com.example.androidhomeapplication.ui.movieDetails.MovieDetailsScreen
import com.example.androidhomeapplication.movieRepository
import com.example.androidhomeapplication.navigation.RouterProvider
import com.example.androidhomeapplication.ui.search.SearchScreen
import com.github.terrakok.cicerone.androidx.FragmentScreen
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class FragmentMoviesList : Fragment(R.layout.fragment_movies_list) {
    private val binding by viewBinding(FragmentMoviesListBinding::bind)
    private val viewModel: MoviesListViewModel by viewModels {
        MoviesListViewModelFactory(
            movieRepository
        )
    }

    private val pagingAdapter: MoviesListAdapter = MoviesListAdapter(
        onItemClick = { item ->
            (activity?.application as? RouterProvider)?.router?.navigateTo(MovieDetailsScreen(item.id))
        })

    private var coroutineJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cinemaRv.layoutManager = GridLayoutManager(context, 2)
        binding.cinemaRv.adapter = pagingAdapter

        initListeners()

//        if(viewModel.moviesList==null){
//            loadData()
//        }

        if (!viewModel.currentQueryValue.isNullOrEmpty()){
            binding.textInputLayout.editText?.setText(viewModel.currentQueryValue)
            loadData(viewModel.currentQueryValue)
        }else{
            loadData()
        }
    }

    private fun initListeners() {
        binding.searchButton.setOnClickListener {
            val searchQuery = binding.textInputLayout.editText?.text.toString()
            if (!searchQuery.isNullOrEmpty()) {
                loadData(searchQuery)
            }

        }

        binding.textInputLayout.editText?.doOnTextChanged { inputText, _, _, _ ->
            if (inputText.isNullOrEmpty()) {
                loadData()
            }
        }
    }


    private fun loadData(query: String? = null) {
        coroutineJob?.cancel()

        if (query.isNullOrEmpty()) {
            viewModel.getMovies()
        } else {
            viewModel.searchMovies(query)
        }

        coroutineJob = viewLifecycleOwner.lifecycleScope.launch {
            viewModel.moviesList?.collectLatest { data ->
                pagingAdapter.submitData(data)
            }
        }

    }
}

class MoviesListScreen : FragmentScreen(
    key = "MoviesListScreen",
    fragmentCreator = { fragmentFactory: FragmentFactory -> FragmentMoviesList() }
)