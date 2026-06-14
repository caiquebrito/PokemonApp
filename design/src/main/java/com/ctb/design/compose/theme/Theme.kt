package com.ctb.design.compose.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// Brand-anchored scheme: Pokémon blue is the primary chrome color, yellow the accent, on a
// lavender canvas with white surfaces. Type colors are applied by content via [PokemonTypeColor],
// never through the scheme — keeping brand chrome and content color cleanly separated.
private val LightColorScheme =
    lightColorScheme(
        primary = Palette.BrandBlue,
        onPrimary = Palette.White,
        primaryContainer = Palette.BrandBlueDark,
        onPrimaryContainer = Palette.White,
        secondary = Palette.BrandYellow,
        onSecondary = Palette.BrandNavy,
        tertiary = Palette.BrandBlueDark,
        background = Palette.Lavender,
        onBackground = Palette.Ink,
        surface = Palette.Surface,
        onSurface = Palette.Ink,
        surfaceVariant = Palette.Track,
        onSurfaceVariant = Palette.Ink,
    )

private val DarkColorScheme =
    darkColorScheme(
        primary = Palette.BrandBlue,
        onPrimary = Palette.White,
        secondary = Palette.BrandYellow,
        onSecondary = Palette.BrandNavy,
        tertiary = Palette.BrandBlueDark,
    )

@Composable
fun PokemonTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme =
        when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                val context = LocalContext.current
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            }

            darkTheme -> {
                DarkColorScheme
            }

            else -> {
                LightColorScheme
            }
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}
