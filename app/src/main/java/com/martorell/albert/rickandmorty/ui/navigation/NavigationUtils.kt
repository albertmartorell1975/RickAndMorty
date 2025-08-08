package com.martorell.albert.rickandmorty.ui.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import kotlinx.serialization.Serializable

sealed class Screens {

    @Serializable
    data object CharactersList : Screens()

    @Serializable
    data class CharacterDetail(val id: Int) : Screens()

}

fun NavHostController.navigatePoppingUpToStartDestination(route: Any) {

    navigate(route) {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when re-selecting a previously selected item
        restoreState = true
    }

}