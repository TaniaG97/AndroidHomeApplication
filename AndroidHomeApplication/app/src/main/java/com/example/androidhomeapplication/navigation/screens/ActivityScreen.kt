package com.example.androidhomeapplication.navigation.screens

import android.content.Context
import android.content.Intent
import ru.terrakok.cicerone.android.support.SupportAppScreen

class ActivityScreen(private val intent: Intent) : SupportAppScreen() {

    override fun getActivityIntent(context: Context): Intent? {
        return intent
    }

}