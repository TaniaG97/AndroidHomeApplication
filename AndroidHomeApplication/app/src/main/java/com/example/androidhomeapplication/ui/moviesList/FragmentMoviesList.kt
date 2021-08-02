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
import com.example.androidhomeapplication.reduxPagination.State
import com.example.androidhomeapplication.utils.DataResult
import com.github.terrakok.cicerone.androidx.FragmentScreen
import kotlinx.android.synthetic.main.fragment_movies_list.view.*
import kotlinx.coroutines.MainScope
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
            val cacheResult = viewModel.searchQuery.isEmpty()
            (activity?.application as? RouterProvider)?.router?.navigateTo(MovieDetailsScreen(item.id, cacheResult))
        })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cinemaRv.layoutManager = GridLayoutManager(context, 2)
        binding.cinemaRv.adapter = adapter

        initListeners()
    }


    private fun initListeners() {

        viewModel.viewState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is State.Empty -> {
                    adapter.submitList(emptyList())
                    viewModel.getCachedMovies()
                    Log.d("FragmentMoviesList_LOG", "State.Empty")
                }
                is State.EmptyProgress -> {
                    Log.d("FragmentMoviesList_LOG", "State.EmptyProgress")
                }
                is State.Data<*> -> {
                    val movies = state.data as List<Movie>
                    adapter.submitList(movies)

                    if (viewModel.searchQuery.isEmpty()){
                        viewModel.saveMoviesToCache(movies)
                    }

                    Log.d("FragmentMoviesList_LOG", "State.Data: page - ${state.page}")
                }
                is State.Refresh<*> -> {
                    Log.d("FragmentMoviesList_LOG", "State.Refresh: page - ${state.page}")
                }
                is State.NewPageProgress<*> -> {
                    Log.d("FragmentMoviesList_LOG", "State.NewPageProgress: page - ${state.page}")
                }
                is State.FullData<*> -> {
                    Log.d("FragmentMoviesList_LOG", "State.FullData: page - ${state.page}")
                }
            }
        }

        viewModel.cachedMovies.observe(viewLifecycleOwner) { movies ->
            if (viewModel.viewState.value == State.Empty){
                adapter.submitList(movies)
                viewModel.refresh()
                Log.d("FragmentMoviesList_LOG", "Show Cached data and call refresh")
            }
        }

        binding.textInputLayout.textInputEditText.textChanges()
            .debounce(300)
            .map { charSequence -> charSequence?.toString() }
            .distinctUntilChanged()
            .map { value ->
                if (viewModel.searchQuery != value) {
                    adapter.submitList(emptyList())
                    viewModel.restart(value ?: "")
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
                        viewModel.loadNextPage()
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