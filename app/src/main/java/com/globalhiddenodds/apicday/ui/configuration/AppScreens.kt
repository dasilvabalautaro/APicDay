package com.globalhiddenodds.apicday.ui.configuration

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

enum class AppScreens(val icon: ImageVector) {
    Splash(
        icon = Icons.Filled.CloudDownload
    ),
    PicDay(
        icon = Icons.Filled.CollectionsBookmark
    ),
    Close(
        icon = Icons.Filled.Logout
    ),
    Settings(
        icon = Icons.Filled.Settings
    );

    companion object {
        fun fromRoute(route: String?): AppScreens =
            when(route?.substringBefore("/")) {
                Splash.name -> Splash
                PicDay.name -> PicDay
                Close.name -> Close
                Settings.name -> Settings
                null -> Splash
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}