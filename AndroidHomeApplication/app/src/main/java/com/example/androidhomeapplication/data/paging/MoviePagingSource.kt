package com.example.androidhomeapplication.data.paging

import com.example.androidhomeapplication.data.models.Movie
import com.example.androidhomeapplication.data.repository.MoviesRepository

class MoviePagingSource(
    private val moviesRepository: MoviesRepository
) : BasePagingSource<Movie>() {

    override suspend fun loadData(pageKey: Int): List<Movie> =
        moviesRepository.loadMovies(pageKey)
}
