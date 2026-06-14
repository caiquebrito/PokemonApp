package com.ctb.domain.models

/**
 * Lightweight summary used by the Home grid: enough to render a card
 * (name, artwork, and the primary type that drives its gradient color).
 */
data class Pokemon(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val primaryType: PokemonType,
    val secondaryType: PokemonType? = null,
)
