package com.example.androidhomeapplication.ui.moviesList

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.androidhomeapplication.*
import com.example.androidhomeapplication.data.models.Movie
import com.example.androidhomeapplication.databinding.FragmentMoviesListBinding
import com.example.androidhomeapplication.ui.movieDetails.MovieDetailsScreen
import com.example.androidhomeapplication.navigation.RouterProvider
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.moviesFromFlow.collect { moviesDataResult ->
                when (moviesDataResult) {
                    is DataResult.Success<List<Movie>> -> {
                        isLoading = false
                        adapter.submitList(moviesDataResult.value)
                    }
                    is DataResult.EmptyResult -> {
                        isLoading = false
                        showShortToast(R.string.empty_movies_list)
                    }
                    is DataResult.Error -> {
                        isLoading = false
                        Log.e("FragmentMoviesList", "getMoviesList: Failed", moviesDataResult.error)
                        showShortToast(R.string.something_wrong)
                    }
                    is DataResult.Loading -> {
                        isLoading = true
                    }
                }
            }
        }
    }

    private fun initListeners() {

        binding.textInputLayout.textInputEditText.textChanges()
            .debounce(500)
            .map { charSequence -> charSequence?.toString() }
            .distinctUntilChanged()
            .map { value ->
                //search
                //viewModel.loadData(value)
                //pagingAdapter.refresh()
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        binding.cinemaRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    val visibleItemCount = recyclerView.getLayoutManager()!!.getChildCount()
                    val totalItemCount = recyclerView.getLayoutManager()!!.getItemCount()
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