package com.example.androidhomeapplication.ui.moviesList

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.androidhomeapplication.*
import com.example.androidhomeapplication.data.models.Movie
import com.example.androidhomeapplication.databinding.FragmentMoviesListBinding
import com.example.androidhomeapplication.ui.movieDetails.MovieDetailsScreen
import com.example.androidhomeapplication.navigation.RouterProvider
import com.example.androidhomeapplication.utils.DataResult
import com.github.terrakok.cicerone.androidx.FragmentScreen
import kotlinx.android.synthetic.main.fragment_movies_list.view.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FragmentMoviesList : Fragment(R.layout.fragment_movies_list) {
    private val binding by viewBinding(FragmentMoviesListBinding::bind)
    private val viewModel: MoviesListViewModel by viewModels {
        MoviesListViewModelFactory(movieRepository)
    }

    var isLoading = false

    private val adapter: MoviesListAdapter = MoviesListAdapter(
        onItemClick = { item ->
            (activity?.application as? RouterProvider)?.router?.navigateTo(MovieDetailsScreen(item.id))
        })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cinemaRv.layoutManager = GridLayoutManager(context, 2)
        binding.cinemaRv.adapter = adapter

        initListeners()
    }

    private fun initListeners() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.moviesFromFlow.collect { moviesDataResult ->
                when (moviesDataResult) {
                    is DataResult.Success<List<Movie>> -> {
                        if (!viewModel.isSearchModFlow.value){
                            adapter.submitList(moviesDataResult.value)
                        }else{
                            val set = mutableSetOf<Movie>()
                            set.addAll(adapter.currentList)
                            set.addAll(moviesDataResult.value)
                            adapter.submitList(set.toList())
                        }
                    }
                    is DataResult.EmptyResult -> {
                        showShortToast(R.string.empty_movies_list)
                    }
                    else -> {}
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.pageFlow.collect { result ->
                when (result) {
                    is DataResult.Loading -> {
                        isLoading = true
                    }
                    is DataResult.Success<Int> -> {
                        isLoading = false
                        viewModel.lastLoadedPage = result.value
                        Log.d("FragmentMoviesList", "currentPage: " + result.value)
                    }
                    is DataResult.Error -> {
                        isLoading = false
                        Log.e("FragmentMoviesList", "Loading page: Failed", result.error)
                        showShortToast(R.string.something_wrong)
                    }
                    else -> {  }
                }
            }
        }

        binding.textInputLayout.textInputEditText.textChanges()
            .debounce(300)
            .map { charSequence -> charSequence?.toString() }
            .distinctUntilChanged()
            .map { value ->
                if (viewModel.lastQuery != value) {
                    adapter.submitList(emptyList())
                    viewModel.setQuery(value ?: "")
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        binding.cinemaRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 && recyclerView.layoutManager != null) {
                    val layoutManager = recyclerView.layoutManager!!

                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val pastVisibleItem =
                        (recyclerView.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()

                    if (!isLoading && (visibleItemCount + pastVisibleItem) >= totalItemCount) {
                        viewModel.loadMoviePage()
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

}

class MoviesListScreen : FragmentScreen(
    key = "MoviesListScreen",
    fragmentCreator = { fragmentFactory: FragmentFactory -> FragmentMoviesList() }
)