package com.android.academy.fundamentals.homework.data

import android.content.Context
import com.example.androidhomeapplication.data.*
import com.example.androidhomeapplication.data.JsonActor
import com.example.androidhomeapplication.data.JsonGenre
import com.example.androidhomeapplication.data.JsonMovie
import com.example.androidhomeapplication.data.mapToActor
import com.example.androidhomeapplication.models.Actor
import com.example.androidhomeapplication.models.Genre
import com.example.androidhomeapplication.models.Movie
import com.example.androidhomeapplication.readAssetFileToString
import kotlinx.coroutines.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.util.concurrent.atomic.AtomicReference

interface MovieRepository {
    suspend fun loadMovies(): List<Movie>
    suspend fun loadMovie(movieId: Long): Movie?
}

internal interface MovieRepositoryProvider {
    val movieRepository: MovieRepository
}

class JsonMovieRepository(private val context: Context) : MovieRepository {
    private val jsonFormat = Json { ignoreUnknownKeys = true }
    private val movies: AtomicReference<List<Movie>?> = AtomicReference<List<Movie>?>()

    override suspend fun loadMovies(): List<Movie> = withContext(Dispatchers.IO) {
        val cachedMovies = movies.get()
        if (cachedMovies != null) {
            cachedMovies
        } else {
            val moviesFromJson = loadMoviesFromJsonFile()
            movies.set(moviesFromJson)
            moviesFromJson
        }
    }

    override suspend fun loadMovie(movieId: Long): Movie? =
        loadMovies().find { movie: Movie -> movie.id == movieId }

    private suspend fun loadMoviesFromJsonFile(): List<Movie> = coroutineScope {
        val genresMap = async { loadData(fileName = "genres.json", JsonGenre::mapToGenre) }
        val actorsMap = async { loadData(fileName = "people.json", JsonActor::mapToActor) }

        parseMovies(genresMap.await(), actorsMap.await())
    }

    private suspend fun parseMovies(
        genreData: List<Genre>,
        actors: List<Actor>
    ): List<Movie> {
        val genresMap = genreData.associateBy(Genre::id)
        val actorsMap = actors.associateBy(Actor::id)

        return loadData(fileName = "data.json") { jsonMovie: JsonMovie ->
            jsonMovie.mapToMovie(genresMap, actorsMap)
        }
    }

    private suspend inline fun <reified I, reified O> loadData(
        fileName: String,
        crossinline transform: (I) -> O
    ): List<O> = withContext(Dispatchers.IO) {
        val data = context.readAssetFileToString(fileName)
        val jsonActors: List<I> = jsonFormat.decodeFromString(data)
        jsonActors.map(transform)
    }
}
