package com.example.androidhomeapplication.ui.search

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
import com.example.androidhomeapplication.data.paging.SearchPagingSource
import com.example.androidhomeapplication.data.repository.MoviesRepository
import com.example.androidhomeapplication.ui.moviesList.MoviesListViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    private var currentQueryValue: String? = null
    private var currentSearchResult: Flow<PagingData<Movie>>? = null
    private val pagingConfig: PagingConfig by lazy {
        PagingConfig(pageSize = Constants.DEFAULT_ITEM_PER_PAGE)
    }

    fun searchMovies(queryString: String): Flow<PagingData<Movie>> {
        val lastResult = currentSearchResult
        if (queryString == currentQueryValue && lastResult != null) {
            return lastResult
        }
        currentQueryValue = queryString

        val newResult: Flow<PagingData<Movie>> = Pager(pagingConfig) {
            SearchPagingSource(moviesRepository, queryString)
        }.flow.cachedIn(viewModelScope)

        currentSearchResult = newResult
        return newResult
    }
}

class SearchViewModelFactory(
    private val movieRepository: MoviesRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
        SearchViewModel::class.java -> SearchViewModel(movieRepository)
        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
    } as T
}