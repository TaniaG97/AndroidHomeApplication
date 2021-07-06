package com.example.androidhomeapplication

import android.app.Application
import com.example.androidhomeapplication.data.db.FilmDatabase
import com.example.androidhomeapplication.data.remote.RetrofitBuilder
import com.example.androidhomeapplication.data.remote.services.ConfigurationService
import com.example.androidhomeapplication.data.remote.services.MoviesService
import com.example.androidhomeapplication.data.repository.RepositoryProvider
import com.example.androidhomeapplication.data.repository.MoviesRepository
import com.example.androidhomeapplication.navigation.NavigatorHolderProvider
import com.example.androidhomeapplication.navigation.RouterProvider
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import retrofit2.Retrofit

class App : Application(), RouterProvider, NavigatorHolderProvider, RepositoryProvider{
    private val cicerone = Cicerone.create()

    override val router: Router get() = cicerone.router
    override val navigatorHolder: NavigatorHolder get() = cicerone.getNavigatorHolder()

    override val movieRepository: MoviesRepository by lazy {
        val retrofit: Retrofit = RetrofitBuilder.buildRetrofit()
        MoviesRepository(
            FilmDatabase.getDatabase(this),
            retrofit.create(MoviesService::class.java),
            retrofit.create(ConfigurationService::class.java)
        )
    }

}