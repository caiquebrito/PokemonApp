package com.ctb.data

import com.ctb.domain.models.EvolutionChain
import com.ctb.domain.models.Pokemon
import com.ctb.domain.models.PokemonDetail
import com.ctb.domain.models.PokemonPage
import kotlinx.coroutines.flow.Flow

interface PokemonRemoteDataSource {
    fun getPokemonPage(
        limit: Int,
        offset: Int,
    ): Flow<PokemonPage>

    fun getPokemonDetail(id: Int): Flow<PokemonDetail>

    fun searchPokemonByName(name: String): Flow<Pokemon>

    fun getEvolutionChain(pokemonId: Int): Flow<EvolutionChain>
}
