package com.ctb.design.compose.theme

import androidx.compose.ui.graphics.Color

/**
 * Raw color palette — the single place literal hex values live.
 *
 * Do NOT reference these directly from UI code. Brand chrome is consumed through the semantic
 * [androidx.compose.material3.MaterialTheme.colorScheme] tokens (primary, secondary, background,
 * surface, …) wired in [PokemonTheme], and content colors through [PokemonTypeColor]. Centralizing
 * the literals here keeps the palette easy to identify, audit, and re-theme.
 */
internal object Palette {
    // Pokémon brand
    val BrandBlue = Color(0xFF3D7DCA)
    val BrandBlueDark = Color(0xFF2A75BB)
    val BrandNavy = Color(0xFF1A3A6B)
    val BrandYellow = Color(0xFFFFCB05)

    // Neutrals / canvas
    val Lavender = Color(0xFFC9D2F0)
    val Surface = Color(0xFFFFFFFF)
    val Ink = Color(0xFF2B2B2B)
    val Track = Color(0xFFE0E0E0)
    val White = Color(0xFFFFFFFF)
}
