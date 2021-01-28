package com.example.androidhomeapplication

import android.app.Application
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router

class App: Application() {

    companion object {
        var INSTANCE: App? = null
    }

    private var cicerone: Cicerone<Router>? = null

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        initCicerone()
    }

    private fun initCicerone() {
        cicerone = Cicerone.create()
    }

    fun getNavigatorHolder(): NavigatorHolder? {
        return cicerone?.navigatorHolder
    }

    fun getRouter(): Router? {
        return cicerone?.router
    }
}