package com.example.androidhomeapplication

import android.app.Application
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

class App : Application(), RouterProvider, NavigatorHolderProvider, RepositoryProvider {
    private val cicerone = Cicerone.create()

    override val router: Router get() = cicerone.router
    override val navigatorHolder: NavigatorHolder get() = cicerone.getNavigatorHolder()

    private val retrofit: Retrofit = RetrofitBuilder.buildRetrofit()
    private val configurationService: ConfigurationService by lazy {  retrofit.create(ConfigurationService::class.java) }
    private val movieService: MoviesService by lazy { retrofit.create(MoviesService::class.java) }

    override val movieRepository: MoviesRepository by lazy { MoviesRepository(retrofit, configurationService, movieService) }
}