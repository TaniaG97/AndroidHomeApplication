package com.example.androidhomeapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidhomeapplication.navigation.screens.Screens
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppNavigator

class MainActivity : AppCompatActivity() {

    lateinit var navigator: SupportAppNavigator
    lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigator = SupportAppNavigator(this, supportFragmentManager, R.id.main_container)
        router = App.INSTANCE?.getRouter()!!
        router.navigateTo(Screens.MoviesList())
    }

    override fun onResume() {
        super.onResume()
        App.INSTANCE?.getNavigatorHolder()?.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        App.INSTANCE?.getNavigatorHolder()?.removeNavigator()
    }

    override fun onBackPressed() {
        router.exit()
    }
}