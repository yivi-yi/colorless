package com.colorless.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightScheme = lightColorScheme(
    primary = SlateBlue,
    onPrimary = PaperWhite,
    secondary = GrayBlue,
    onSecondary = PaperWhite,
    background = Ivory,
    onBackground = DarkSlate,
    surface = PaperWhite,
    onSurface = DarkSlate,
    surfaceVariant = CloudBlue,
    onSurfaceVariant = DarkSlate,
    outline = Oat,
    outlineVariant = MistBlue,
)

private val DarkScheme = darkColorScheme(
    primary = GrayBlue,
    onPrimary = NightBg,
    secondary = MistBlue,
    onSecondary = NightBg,
    background = NightBg,
    onBackground = NightText,
    surface = DarkSlate,
    onSurface = NightText,
    surfaceVariant = DeepSlate,
    onSurfaceVariant = NightText,
    outline = NightLine,
    outlineVariant = DeepSlate,
)

@Composable
fun ColorlessTheme(
    dark: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (dark) DarkScheme else LightScheme,
        typography = ColorlessType,
        content = content,
    )
}
