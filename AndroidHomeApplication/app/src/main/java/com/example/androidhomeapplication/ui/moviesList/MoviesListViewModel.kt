package com.example.androidhomeapplication.ui.moviesList

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.androidhomeapplication.data.constans.Constants
import com.example.androidhomeapplication.data.models.Movie
import com.example.androidhomeapplication.data.paging.MoviePagingSource
import com.example.androidhomeapplication.data.paging.SearchPagingSource
import com.example.androidhomeapplication.data.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow

class MoviesListViewModel(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    private val pagingConfig: PagingConfig =
        PagingConfig(pageSize = Constants.DEFAULT_ITEM_PER_PAGE)

    private var currentQueryValue: String? = null
    val queryValue: String? get() = currentQueryValue

    private var currentMoviesList: Flow<PagingData<Movie>>? = null
    val moviesList:  Flow<PagingData<Movie>>? get() = currentMoviesList

    fun loadData(queryString: String? = null) {
        if (queryString.isNullOrEmpty()) {
            currentQueryValue = null
            currentMoviesList = Pager(pagingConfig) {
                MoviePagingSource(moviesRepository)
            }.flow.cachedIn(viewModelScope)
        } else if (queryString != currentQueryValue) {
            currentQueryValue = queryString
            currentMoviesList = Pager(pagingConfig) {
                SearchPagingSource(moviesRepository, queryString)
            }.flow.cachedIn(viewModelScope)
        }
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