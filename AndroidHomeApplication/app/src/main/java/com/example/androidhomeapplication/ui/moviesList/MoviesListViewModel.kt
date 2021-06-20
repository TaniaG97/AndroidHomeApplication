package com.example.androidhomeapplication.ui.moviesList

import androidx.lifecycle.*
import com.example.androidhomeapplication.DataResult
import com.example.androidhomeapplication.data.models.Movie
import com.example.androidhomeapplication.data.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class MoviesListViewModel(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    private val isSearchModFlow = MutableStateFlow(false)

    var moviesFromFlow: Flow<DataResult<List<Movie>>> =
        isSearchModFlow.flatMapLatest { isSearchMod ->
            if (isSearchMod) {
                moviesRepository.searchFlow
            } else {
                moviesRepository.popularMoviesFlow
            }
        }

    var popularMoviesFlow = moviesRepository.loadMoviePageFlow

    var lastQuery = ""
    var lastLoadedPage = 0


    fun loadMoviePage() {
        viewModelScope.launch {
            moviesRepository.loadMoviePage(lastLoadedPage + 1)
        }
    }

    fun setQuery(query: String) {
        lastQuery = query
        viewModelScope.launch {
            isSearchModFlow.value = query.isNotEmpty()
            if (isSearchModFlow.value) {
                moviesRepository.emitSearchQuery(query)
            }
            lastLoadedPage = 0
            loadMoviePage()
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