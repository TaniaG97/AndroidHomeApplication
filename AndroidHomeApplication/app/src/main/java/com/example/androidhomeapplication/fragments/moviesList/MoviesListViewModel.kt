package com.example.androidhomeapplication.fragments.moviesList

import android.util.Log
import androidx.lifecycle.*
import com.android.academy.fundamentals.homework.data.MovieRepository
import com.example.androidhomeapplication.DataResult
import com.example.androidhomeapplication.models.Movie
import kotlinx.coroutines.launch

class MoviesListViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val mutableMovieList = MutableLiveData<DataResult<List<Movie>>>(DataResult.Default())
    val moviesList: LiveData<DataResult<List<Movie>>> get() = mutableMovieList

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