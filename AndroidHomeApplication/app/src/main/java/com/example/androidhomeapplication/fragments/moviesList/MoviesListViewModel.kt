package com.example.androidhomeapplication.fragments.moviesList

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.android.academy.fundamentals.homework.data.MovieRepository
import com.example.androidhomeapplication.models.Movie
import com.example.androidhomeapplication.movieRepository
import com.example.androidhomeapplication.observer.DataResult
import kotlinx.coroutines.launch

class MoviesListViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _mutableMovieList = MutableLiveData<DataResult<List<Movie>>>()
    val moviesList: LiveData<DataResult<List<Movie>>> get() = _mutableMovieList

    fun getMoviesList() {
        _mutableMovieList.value = DataResult(DataResult.State.IN_PROGRESS)

        viewModelScope.launch {
            try {
                val result = movieRepository.getMovies()
                _mutableMovieList.value = DataResult(result)
            } catch (throwable: Throwable) {
                _mutableMovieList.value = DataResult(DataResult.State.ERROR)
                Log.e("MoviesListViewModel", "getMoviesList: Failed", throwable)
            }
        }
    }
}

class MoviesListViewModelFactory(
    private val movieRepository: MovieRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
        MoviesListViewModel::class.java -> MoviesListViewModel(movieRepository)
        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
    } as T
}