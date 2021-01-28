package com.example.androidhomeapplication.navigation

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.example.androidhomeapplication.navigation.container.BaseFragmentContainer
import com.example.androidhomeapplication.navigation.container.MovieContainer
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command
import java.util.*

class AppNavigator(
        activity: FragmentActivity,
        fragmentManager: FragmentManager,
        @IdRes containerId: Int
) : SupportAppNavigator(activity, fragmentManager, containerId) {

    constructor(activity: FragmentActivity, @IdRes containerId: Int) : this(activity, activity.supportFragmentManager, containerId)

    private val containers = LinkedList<BaseFragmentContainer>()

    fun initContainers() {
        val fm = fragmentManager ?: return
        val movieContainer = fm.findFragmentByTag(MovieContainer.TAG) as? MovieContainer
                ?: MovieContainer.newInstance()
        fm.beginTransaction()
                .replace(containerId, movieContainer, MovieContainer.TAG)
                .detach(movieContainer)
                .commitNow()

        containers.add(movieContainer)
    }

    override fun applyCommand(command: Command) {
        if (command is NavigationCommand) {
            val transaction = fragmentManager?.beginTransaction() ?: return
            var wasContainerAttached = false
            containers.forEach { container ->
                if (container.getContainerName() == command.screen.screenKey) {
                    transaction.attach(container)
                    wasContainerAttached = true
                } else {
                    transaction.detach(container)
                }
            }
            if (!wasContainerAttached) {
                throw RuntimeException("Container = ${command.screen.screenKey} not found!")
            }
            transaction.commitNow()
        } else {
            super.applyCommand(command)
        }
    }

}