package com.example.androidhomeapplication

import com.example.androidhomeapplication.models.CastData
import com.example.androidhomeapplication.models.MovieData
import com.example.androidhomeapplication.models.MovieGenres

object DataGenerator {

    fun getMoviesList(): List<MovieData> = listOf(
        MovieData(
            id = 0,
            poster = R.drawable.movie_avengers,
            detailPoster = R.drawable.background_image,
            title = "Avengers: End Game",
            description = "After the devastating events of Avengers: Infinity War, the universe is in ruins. With the help of remaining allies, the Avengers assemble once more in order to reverse Thanos\\' actions and restore balance to the universe.",
            genresList = listOf(
                MovieGenres.ACTION.value,
                MovieGenres.ADVENTURE.value,
                MovieGenres.FANTASY.value
            ),
            ageLimit = 13,
            time = 137,
            isLiked = false,
            starCount = 4,
            reviewCount = 125,
            castList = listOf(
                CastData(id=0, name = "Robert Downey Jr.", image = R.drawable.robert_downey_jr),
                CastData(id=1, name = "Chris Evans", image = R.drawable.chris_evans),
                CastData(id=2, name = "Mark Ruffalo", image = R.drawable.mark_ruffalo),
                CastData(id=3, name = "Chris Hemsworth", image = R.drawable.chris_hemsworth)
            )
        ),
        MovieData(
            id = 1,
            poster = R.drawable.movie_tenet,
            detailPoster = R.drawable.movie_tenet,
            title = "Tenet",
            description = "Interesting Description",
            genresList = listOf(
                MovieGenres.ACTION.value,
                MovieGenres.SCI_FI.value,
                MovieGenres.THRILLER.value
            ),
            ageLimit = 13,
            time = 97,
            isLiked = true,
            starCount = 5,
            reviewCount = 98,
            castList = listOf(
                CastData(id=0, name = "Cast Name", image = R.drawable.background_rect_with_border),
                CastData(id=1, name = "Cast Name", image = R.drawable.background_rect_with_border),
                CastData(id=2, name = "Cast Name", image = R.drawable.background_rect_with_border),
                CastData(id=3, name = "Cast Name", image = R.drawable.background_rect_with_border),
                CastData(id=4, name = "Cast Name", image = R.drawable.background_rect_with_border)
            )
        ),
        MovieData(
            id = 2,
            poster = R.drawable.movie_black_widow,
            detailPoster = R.drawable.movie_black_widow,
            title = "Black Widow",
            description = "Interesting Description",
            genresList = listOf(
                MovieGenres.ACTION.value,
                MovieGenres.ADVENTURE.value,
                MovieGenres.SCI_FI.value
            ),
            ageLimit = 13,
            time = 102,
            isLiked = false,
            starCount = 4,
            reviewCount = 38,
            castList = listOf(
                CastData(id=0, name = "Cast Name", image = R.drawable.background_rect_with_border),
                CastData(id=1, name = "Cast Name", image = R.drawable.background_rect_with_border),
                CastData(id=2, name = "Cast Name", image = R.drawable.background_rect_with_border),
                CastData(id=3, name = "Cast Name", image = R.drawable.background_rect_with_border)
            )
        ),
        MovieData(
            id = 3,
            poster = R.drawable.movie_wonder_woman,
            detailPoster = R.drawable.movie_wonder_woman,
            title = "Wonder Woman 1984",
            description = "Interesting Description",
            genresList = listOf(
                MovieGenres.ACTION.value,
                MovieGenres.ADVENTURE.value,
                MovieGenres.FANTASY.value
            ),
            ageLimit = 13,
            time = 120,
            isLiked = false,
            starCount = 5,
            reviewCount = 74,
            castList = listOf(
                CastData(id=0, name = "Cast Name", image = R.drawable.background_rect_with_border),
                CastData(id=1, name = "Cast Name", image = R.drawable.background_rect_with_border),
                CastData(id=2, name = "Cast Name", image = R.drawable.background_rect_with_border),
                CastData(id=3, name = "Cast Name", image = R.drawable.background_rect_with_border),
                CastData(id=4, name = "Cast Name", image = R.drawable.background_rect_with_border)
            )
        )
    )

}