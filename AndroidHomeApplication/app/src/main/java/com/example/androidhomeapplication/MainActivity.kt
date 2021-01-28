package com.example.androidhomeapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.androidhomeapplication.navigation.AppNavigator
import com.example.androidhomeapplication.navigation.AppRouter
import com.example.androidhomeapplication.navigation.BackButtonListener
import com.example.androidhomeapplication.navigation.RouterProvider
import com.example.androidhomeapplication.navigation.container.MovieContainer
import com.example.androidhomeapplication.navigation.screens.FragmentScreen
import ru.terrakok.cicerone.Cicerone

class MainActivity : AppCompatActivity(), RouterProvider {

    private val cicerone = Cicerone.create(AppRouter())
    private lateinit var appNavigator: AppNavigator

    override val router: AppRouter
        get() = cicerone.router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        appNavigator = AppNavigator(this, R.id.main_container)
        appNavigator.initContainers()

        if (savedInstanceState == null) {
            router.replaceContainer(FragmentScreen(MovieContainer.newInstance()))
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        router.replaceContainer(FragmentScreen(MovieContainer.newInstance()))
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        cicerone.navigatorHolder.setNavigator(appNavigator)
    }

    override fun onPause() {
        super.onPause()
        cicerone.navigatorHolder.removeNavigator()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.main_container)
        if ((fragment != null && fragment is BackButtonListener && fragment.onBackPressed())) {
            return
        } else {
            router.exit()
        }
    }
}