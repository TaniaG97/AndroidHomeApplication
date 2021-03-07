package com.example.androidhomeapplication.ui.moviesList

import androidx.lifecycle.*
import com.example.androidhomeapplication.DataResult
import com.example.androidhomeapplication.data.models.Movie
import com.example.androidhomeapplication.data.repository.MoviesRepository
import kotlinx.coroutines.launch

class MoviesListViewModel(
    private val movieRepository: MoviesRepository
) : ViewModel() {

    private val mutableMovieList = MutableLiveData<DataResult<List<Movie>>>()
    val moviesList: LiveData<DataResult<List<Movie>>> get() = mutableMovieList

    init {
        getMoviesList()
    }

    fun getMoviesList() {
        mutableMovieList.value = DataResult.Loading()

        viewModelScope.launch {
            mutableMovieList.value = try {
                DataResult.Success(movieRepository.getMovies())
            } catch (throwable: Throwable) {
                DataResult.Error(throwable)
            }
        }
    }

    fun searchMovies(searchValue: String) {
        mutableMovieList.value = DataResult.Loading()

        viewModelScope.launch {
            mutableMovieList.value = try {
                DataResult.Success(movieRepository.searchMovies(searchValue))
            } catch (throwable: Throwable) {
                DataResult.Error(throwable)
            }
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