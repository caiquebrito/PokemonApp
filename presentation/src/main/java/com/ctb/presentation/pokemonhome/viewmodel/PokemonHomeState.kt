package com.ctb.presentation.pokemonhome.viewmodel

import com.ctb.common.viewmodel.flow.UIState
import com.ctb.domain.models.Pokemon

data class PokemonHomeState(
    val isLoading: Boolean = false,
    val isPaginating: Boolean = false,
    val isSearching: Boolean = false,
    val pokemons: List<Pokemon> = emptyList(),
    val query: String = "",
    val nextOffset: Int? = 0,
    val hasMore: Boolean = true,
) : UIState {
    /** Search filters only the already-loaded grid, by name. */
    val visiblePokemons: List<Pokemon>
        get() =
            if (query.isBlank()) {
                pokemons
            } else {
                pokemons.filter { it.name.contains(query.trim(), ignoreCase = true) }
            }
}
