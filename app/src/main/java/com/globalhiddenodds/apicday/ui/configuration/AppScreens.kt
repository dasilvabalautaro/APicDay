package com.globalhiddenodds.apicday.ui.configuration

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PictureInPicture
import androidx.compose.ui.graphics.vector.ImageVector

enum class AppScreens(val icon: ImageVector) {
    Splash(
        icon = Icons.Filled.Download
    ),
    PicDay(
        icon = Icons.Filled.PictureInPicture
    ),
    Close(
        icon = Icons.Filled.Close
    );

    companion object {
        fun fromRoute(route: String?): AppScreens =
            when(route?.substringBefore("/")) {
                Splash.name -> Splash
                PicDay.name -> PicDay
                Close.name -> Close
                null -> Splash
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}