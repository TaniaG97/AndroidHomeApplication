package com.example.androidhomeapplication.fragments.movieDetails

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.android.academy.fundamentals.homework.data.MovieRepository
import com.example.androidhomeapplication.models.Movie
import com.example.androidhomeapplication.observer.DataResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieDetailsViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _mutableMovie = MutableLiveData<DataResult<Movie>>()
    val movie: LiveData<DataResult<Movie>> get() = _mutableMovie

    fun getMovieDetails(movieId:Long) {
        _mutableMovie.value = DataResult(DataResult.State.IN_PROGRESS)

        viewModelScope.launch {
            try {
                val result = movieRepository.getMovie(movieId)
                _mutableMovie.value = DataResult(result)

            } catch (throwable: Throwable) {
                _mutableMovie.value = DataResult(DataResult.State.ERROR)
                Log.e("MovieDetailsViewModel", "getMovieDetails: Failed", throwable)
            }
        }
    }
}

class MovieDetailsViewModelFactory(
    private val movieRepository: MovieRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
        MovieDetailsViewModel::class.java -> MovieDetailsViewModel(movieRepository)
        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
    } as T
}