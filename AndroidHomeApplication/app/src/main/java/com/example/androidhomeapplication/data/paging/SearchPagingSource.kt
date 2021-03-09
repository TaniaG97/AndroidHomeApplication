package com.example.androidhomeapplication.data.paging

import com.example.androidhomeapplication.data.models.Movie
import com.example.androidhomeapplication.data.repository.MoviesRepository

class SearchPagingSource(
    private val moviesRepository: MoviesRepository,
    private val queryString: String
) : BasePagingSource<Movie>() {

    override suspend fun loadData(params: LoadParams<Int>): List<Movie> {
        return moviesRepository.searchMovies(queryString,params.key ?: getFirstPage())
    }
}