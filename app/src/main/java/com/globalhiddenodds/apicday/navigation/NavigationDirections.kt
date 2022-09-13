package com.globalhiddenodds.apicday.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

object NavigationDirections {
    const val KEY_URL = "url"
    const val routeZoom = "zoom/{$KEY_URL}"
    val argumentsZoom = listOf(navArgument(KEY_URL){
        type = NavType.StringType })

    val main = object: NavigationCommand {
        override val arguments: List<NamedNavArgument>
            get() = emptyList()
        override val destination: String
            get() = "main"
    }
    fun zoom(url: String? = null) = object: NavigationCommand {
        override val arguments: List<NamedNavArgument>
            get() = argumentsZoom
        override val destination: String
            get() = "zoom/$url"
    }
    val best = object: NavigationCommand {
        override val arguments: List<NamedNavArgument>
            get() = emptyList()
        override val destination: String
            get() = "best"
    }
}