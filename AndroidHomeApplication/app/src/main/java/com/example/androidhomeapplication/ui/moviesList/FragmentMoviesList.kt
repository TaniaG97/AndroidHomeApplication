package com.example.androidhomeapplication.ui.moviesList

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.androidhomeapplication.R
import com.example.androidhomeapplication.databinding.FragmentMoviesListBinding
import com.example.androidhomeapplication.ui.movieDetails.MovieDetailsScreen
import com.example.androidhomeapplication.movieRepository
import com.example.androidhomeapplication.navigation.RouterProvider
import com.example.androidhomeapplication.textChanges
import com.github.terrakok.cicerone.androidx.FragmentScreen
import kotlinx.android.synthetic.main.fragment_movies_list.view.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FragmentMoviesList : Fragment(R.layout.fragment_movies_list) {
    private val binding by viewBinding(FragmentMoviesListBinding::bind)
    private val viewModel: MoviesListViewModel by viewModels {
        MoviesListViewModelFactory(movieRepository)
    }

    private val pagingAdapter: MoviesListAdapter = MoviesListAdapter(
        onItemClick = { item ->
            (activity?.application as? RouterProvider)?.router?.navigateTo(MovieDetailsScreen(item.id))
        })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cinemaRv.layoutManager = GridLayoutManager(context, 2)
        binding.cinemaRv.adapter = pagingAdapter

        initListeners()

    }

    private fun initListeners() {

        binding.textInputLayout.textInputEditText.textChanges()
            .debounce(500)
            .map { charSequence -> charSequence?.toString() }
            .distinctUntilChanged()
            .flatMapLatest { value ->
                flow {
                    viewModel.loadData(value)
                    pagingAdapter.refresh()
                    emit(value)
                }
            }

            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.movieItems.collectLatest { data ->
                pagingAdapter.submitData(data)
            }
        }
    }
}

class MoviesListScreen : FragmentScreen(
    key = "MoviesListScreen",
    fragmentCreator = { fragmentFactory: FragmentFactory -> FragmentMoviesList() }
)