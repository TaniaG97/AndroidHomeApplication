package com.example.androidhomeapplication.navigation
import androidx.collection.ArrayMap
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router

object LocalCiceroneHolder {

    private val containers = ArrayMap<String, Cicerone<Router>>()

    fun getCicerone(containerTag: String): Cicerone<Router> {
        if (!containers.containsKey(containerTag)) {
            val appRouter = Router()
            containers[containerTag] = Cicerone.create(appRouter)
        }
        return containers[containerTag]!!
    }

}