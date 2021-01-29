package com.example.androidhomeapplication

import android.app.Application
import com.example.androidhomeapplication.navigation.NavigatorHolderProvider
import com.example.androidhomeapplication.navigation.RouterProvider
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router

class App : Application(), RouterProvider, NavigatorHolderProvider {
    private val cicerone = Cicerone.create()
    override val router: Router get() = cicerone.router
    override val navigatorHolder: NavigatorHolder get() = cicerone.getNavigatorHolder()
}