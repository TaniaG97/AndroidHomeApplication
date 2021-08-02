package com.example.androidhomeapplication.ui.moviesList

import androidx.lifecycle.*
import com.example.androidhomeapplication.data.models.Movie
import com.example.androidhomeapplication.data.repository.MoviesRepository
import com.example.androidhomeapplication.reduxPagination.Action
import com.example.androidhomeapplication.reduxPagination.SideEffect
import com.example.androidhomeapplication.reduxPagination.State
import com.example.androidhomeapplication.reduxPagination.Store
import kotlinx.coroutines.launch

class MoviesListViewModel(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    private val mutableViewState = MutableLiveData<State>(State.Empty)
    val viewState: LiveData<State> = mutableViewState

    private val mutableErrorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = mutableErrorMessage

    private val mutableCachedMovies = MutableLiveData<List<Movie>>()
    val cachedMovies: LiveData<List<Movie>> = mutableCachedMovies

    var searchQuery = ""

    private val pagingStore = Store<Movie>().apply {
        render = { state -> mutableViewState.postValue(state) }

        sideEffects = { sideEffect ->
            when (sideEffect) {
                is SideEffect.LoadPage -> loadNewPage(sideEffect.page)
                is SideEffect.ErrorEvent -> {
                    mutableErrorMessage.postValue(sideEffect.throwable.message)
                }
            }
        }
    }

//    init {
//        refresh()
//    }

    fun getCachedMovies() {
        viewModelScope.launch {
            mutableCachedMovies.postValue(moviesRepository.getCachedMovies())
        }
    }

    private fun loadNewPage(page: Int) {
        viewModelScope.launch {
            val moviesListResult = moviesRepository.loadMovies(searchQuery, page)
            pagingStore.proceed(Action.NewPage(page, moviesListResult))
        }
    }

    fun restart(newSearchQuery: String) {
        searchQuery = newSearchQuery
        pagingStore.proceed(Action.Restart)
    }

    fun refresh() = pagingStore.proceed(Action.Refresh)
    fun loadNextPage() = pagingStore.proceed(Action.LoadMore)

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

//enum class MovieApiCallType{
//    POPULAR,
//    SEARCH
//}