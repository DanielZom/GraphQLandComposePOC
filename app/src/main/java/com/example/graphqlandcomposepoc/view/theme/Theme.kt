package com.example.graphqlandcomposepoc.view.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = SpaceXBlue,
    primaryVariant = SpaceXBlueDark,
    secondary = White,
    background = Black,
    surface = Black
)

private val LightColorPalette = lightColors(
    primary = SpaceXBlue,
    primaryVariant = SpaceXBlueDark,
    secondary = White,
    background = White,
    surface = White
)

@Composable
fun GraphQLAndComposePOCTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
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