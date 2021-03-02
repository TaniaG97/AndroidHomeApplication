package com.example.androidhomeapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.androidhomeapplication.ui.moviesList.MoviesListScreen
import com.example.androidhomeapplication.navigation.BackButtonListener
import com.example.androidhomeapplication.navigation.NavigatorHolderProvider
import com.example.androidhomeapplication.navigation.RouterProvider
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.androidx.AppNavigator

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val navigatorHolder: NavigatorHolder? get() = (application as? NavigatorHolderProvider)?.navigatorHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            (application as? RouterProvider)?.router?.newRootScreen(MoviesListScreen())
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder?.setNavigator(AppNavigator(this, R.id.main_container))
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder?.removeNavigator()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.main_container)
        if ((fragment as? BackButtonListener)?.onBackPressed() != true) {
            super.onBackPressed()
        }
    }
}