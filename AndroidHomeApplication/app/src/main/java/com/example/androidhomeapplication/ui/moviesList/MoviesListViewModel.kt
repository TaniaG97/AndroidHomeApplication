package com.example.androidhomeapplication.ui.moviesList

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.androidhomeapplication.DataResult
import com.example.androidhomeapplication.data.constans.Constants
import com.example.androidhomeapplication.data.models.Movie
import com.example.androidhomeapplication.data.paging.BasePagingSource
import com.example.androidhomeapplication.data.paging.MoviePagingSource
import com.example.androidhomeapplication.data.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MoviesListViewModel(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    private val pagingConfig: PagingConfig = PagingConfig(pageSize = Constants.DEFAULT_ITEM_PER_PAGE)


    val movieItems: Flow<PagingData<Movie>> by lazy {
        Pager(pagingConfig) {
            MoviePagingSource(moviesRepository)
        }.flow.cachedIn(viewModelScope)
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