package com.example.androidhomeapplication.ui.movieDetails

import android.util.Log
import androidx.lifecycle.*
import com.example.androidhomeapplication.data.repository.BaseRepository
import com.example.androidhomeapplication.DataResult
import com.example.androidhomeapplication.data.models.MovieDetails
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    private val movieRepository: BaseRepository,
    private val movieId: Int
) : ViewModel() {

    private val mutableMovie = MutableLiveData<DataResult<MovieDetails>>()
    val movie: LiveData<DataResult<MovieDetails>> get() = mutableMovie

    init {
        getMovieDetails()
    }

    private fun getMovieDetails() {
        mutableMovie.value = DataResult.Loading()

        viewModelScope.launch {
            mutableMovie.value = try {
                val result = movieRepository.getMovie(movieId)
                if (result != null) {
                    DataResult.Success(result)
                } else {
                    DataResult.EmptyResult()
                }

            } catch (throwable: Throwable) {
                Log.e("MovieDetailsViewModel", "getMovieDetails: Failed", throwable)
                DataResult.Error(throwable)
            }
        }
    }
}

class MovieDetailsViewModelFactory(
    private val movieRepository: BaseRepository,
    private val movieId: Int
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
        MovieDetailsViewModel::class.java -> MovieDetailsViewModel(movieRepository, movieId)
        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
    } as T
}