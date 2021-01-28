package com.example.androidhomeapplication.navigation

import com.example.androidhomeapplication.navigation.screens.FragmentScreen
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.commands.Command


class AppRouter : Router() {

    fun replaceContainer(screen: FragmentScreen) {
        val command = NavigationCommand(screen)
        executeCommands(command)
    }

}

class NavigationCommand(val screen: FragmentScreen) : Command