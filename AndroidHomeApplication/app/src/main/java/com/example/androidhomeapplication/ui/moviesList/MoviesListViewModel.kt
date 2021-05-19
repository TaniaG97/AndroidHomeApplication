package com.example.androidhomeapplication.ui.moviesList

import androidx.lifecycle.*
import com.example.androidhomeapplication.DataResult
import com.example.androidhomeapplication.data.models.Movie
import com.example.androidhomeapplication.data.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MoviesListViewModel(
    private val moviesRepository: MoviesRepository
) : ViewModel() {
//    private val _moviesList = MutableLiveData<DataResult<List<Movie>>>()
//    val moviesList: LiveData<DataResult<List<Movie>>> get() = _moviesList

    val moviesFromFlow: Flow<DataResult<List<Movie>>> = moviesRepository.moviesFlow
    var currentPage = 0

    init {
//        loadMoviePage()
    }

    fun loadMoviePage() {
        currentPage++
        viewModelScope.launch {
            moviesRepository.loadMoviePage(currentPage)
        }
    }

//    init {
//        getMoviesList("",1)
//    }
//
//    fun getMoviesList(query:String, page:Int) {
//        _moviesList.value = DataResult.Loading()
//
//        viewModelScope.launch {
//            _moviesList.value = try {
//                DataResult.Success(moviesRepository.loadMovies(query,page))
//            } catch (throwable: Throwable) {
//                DataResult.Error(throwable)
//            }
//        }
//    }
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