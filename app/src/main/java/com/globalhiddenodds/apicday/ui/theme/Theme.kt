package com.globalhiddenodds.apicday.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = Blue500,
    primaryVariant = Blue700,
    secondary = Green200,
    secondaryVariant = Green900,
    error = Error,
    background = Background,
    surface = SurfaceNight,
    onPrimary = OnPrimaryNight,
    onSecondary = OnSecondary,
    onBackground = OnBackgroundNight,
    onSurface = OnSurfaceNight,
)

private val LightColorPalette = lightColors(
    primary = Blue200,
    primaryVariant = Blue700,
    secondary = Green200,
    secondaryVariant = Green200,
    error = Error,
    background = Background,
    surface = SurfaceDay,
    onPrimary = OnPrimaryDay,
    onSecondary = OnSecondary,
    onBackground = OnBackgroundDay,
    onSurface = OnSurfaceDay,

    )

@Composable
fun APicDayTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}