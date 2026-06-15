package com.ctb.domain.repositories

import com.ctb.domain.models.Pokemon
import com.ctb.domain.models.PokemonDetail
import com.ctb.domain.models.PokemonPage
import kotlinx.coroutines.flow.Flow

/** Source of truth for Pokémon listing, detail, and search data. */
interface PokemonRepository {
    fun getPokemonPage(
        limit: Int,
        offset: Int,
    ): Flow<PokemonPage>

    fun getPokemonDetail(id: Int): Flow<PokemonDetail>

    /** Looks up a single Pokémon by exact name (used when it isn't in the loaded pages yet). */
    fun searchPokemonByName(name: String): Flow<Pokemon>
}
