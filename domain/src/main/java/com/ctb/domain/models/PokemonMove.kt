package com.ctb.domain.models

/**
 * A move the Pokémon learns by leveling up (its "main" moveset).
 * [levelLearnedAt] is the earliest level the move is learned across game versions.
 */
data class PokemonMove(
    val name: String,
    val levelLearnedAt: Int,
)
