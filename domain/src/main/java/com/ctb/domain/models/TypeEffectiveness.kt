package com.ctb.domain.models

/**
 * Type matchups for [type]: which types it hits hard/weakly/not-at-all when attacking, and
 * which types hit it hard/weakly/not-at-all when defending.
 */
data class TypeEffectiveness(
    val type: PokemonType,
    val superEffectiveAgainst: List<PokemonType>,
    val notVeryEffectiveAgainst: List<PokemonType>,
    val noEffectAgainst: List<PokemonType>,
    val weaknesses: List<PokemonType>,
    val resistances: List<PokemonType>,
    val immunities: List<PokemonType>,
)
