package com.example.androidhomeapplication.ui.moviesList

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.androidhomeapplication.data.constans.Constants
import com.example.androidhomeapplication.data.models.Movie
import com.example.androidhomeapplication.data.paging.MoviesPagingSource
import com.example.androidhomeapplication.data.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce

class MoviesListViewModel(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    private var searchPagingSource = MoviesPagingSource(moviesRepository, null)

    val movieItems: Flow<PagingData<Movie>> by lazy {
        Pager(PagingConfig(pageSize = Constants.DEFAULT_ITEM_PER_PAGE)) {
            searchPagingSource
        }.flow.cachedIn(viewModelScope).debounce(500)
    }

    fun loadData(queryString: String?) {
        searchPagingSource = MoviesPagingSource(moviesRepository, queryString)
    }
}

class MoviesListViewModelFactory(
    private val movieRepository: MoviesRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
        MoviesListViewModel::class.java -> MoviesListViewModel(movieRepository)
        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
    } as T
}