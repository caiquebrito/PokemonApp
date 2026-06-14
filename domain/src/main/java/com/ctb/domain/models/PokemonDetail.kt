package com.ctb.domain.models

/**
 * Full information for the Detail screen.
 * [heightDecimetres] and [weightHectograms] use PokeAPI's raw units (1 dm = 0.1 m, 1 hg = 0.1 kg).
 */
data class PokemonDetail(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val types: List<PokemonType>,
    val heightDecimetres: Int,
    val weightHectograms: Int,
    val stats: List<PokemonStat>,
    val moves: List<PokemonMove>,
) {
    val primaryType: PokemonType
        get() = types.firstOrNull() ?: PokemonType.UNKNOWN
}
