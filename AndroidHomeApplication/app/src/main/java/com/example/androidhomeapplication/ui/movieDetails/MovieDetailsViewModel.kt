package com.example.androidhomeapplication.ui.movieDetails

import android.util.Log
import androidx.lifecycle.*
import com.example.androidhomeapplication.utils.DataResult
import com.example.androidhomeapplication.data.models.MovieDetails
import com.example.androidhomeapplication.data.repository.MoviesRepository
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    private val movieRepository: MoviesRepository,
    private val movieId: Long,
    private val cacheResult: Boolean
) : ViewModel() {

    private val mutableMovie = MutableLiveData<DataResult<MovieDetails>>()
    val movie: LiveData<DataResult<MovieDetails>> get() = mutableMovie

    init {
        getMovieDetails()
    }

    private fun getMovieDetails() {
        getCachedMovieDetails(movieId)
        getMovieDetails(movieId)
    }

    private fun getCachedMovieDetails(movieId: Long) {
        viewModelScope.launch {
            mutableMovie.value = try {
                val result = movieRepository.getCachedMovieById(movieId)
                if (result != null) {
                    DataResult.Success(result)
                } else {
                    Log.e("MovieDetailsViewModel", "no cached movie details")
                    DataResult.Error(Throwable("no cached movie details"))
                }
            } catch (throwable: Throwable) {
                Log.e("MovieDetailsViewModel", "getMovieDetails: Failed", throwable)
                DataResult.Error(throwable)
            }
        }
    }

    private fun getMovieDetails(movieId: Long) {
        viewModelScope.launch {
            mutableMovie.value = try {
                val result = movieRepository.loadMovieById(movieId)
                if (cacheResult) {
                    movieRepository.saveMovieDetailsToCache(result)
                }
                DataResult.Success(result)
            } catch (throwable: Throwable) {
                Log.e("MovieDetailsViewModel", "getMovieDetails: Failed", throwable)
                DataResult.Error(throwable)
            }
        }
    }
}

class MovieDetailsViewModelFactory(
    private val movieRepository: MoviesRepository,
    private val movieId: Long,
    private val cacheResult: Boolean
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
        MovieDetailsViewModel::class.java -> MovieDetailsViewModel(
            movieRepository,
            movieId,
            cacheResult
        )
        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
    } as T
}