package com.ctb.domain.models

/**
 * A single base stat (hp, attack, defense, special-attack, special-defense, speed).
 * [baseValue] ranges roughly 1..255 in PokeAPI, useful for drawing proportional stat bars.
 */
data class PokemonStat(
    val name: String,
    val baseValue: Int,
)
