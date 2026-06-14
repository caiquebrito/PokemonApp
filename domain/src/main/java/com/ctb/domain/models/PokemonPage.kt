package com.ctb.domain.models

/**
 * One page of the paginated Pokémon list.
 * [nextOffset] is the offset to request for the following page, or `null` when [hasMore] is false.
 */
data class PokemonPage(
    val items: List<Pokemon>,
    val nextOffset: Int?,
    val hasMore: Boolean,
)
