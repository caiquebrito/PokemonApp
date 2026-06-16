package com.ctb.domain.usecase

import com.ctb.commonkotlin.usecases.FlowUseCase
import com.ctb.commonkotlin.usecases.Result
import com.ctb.domain.models.EvolutionChain
import com.ctb.domain.repositories.PokemonRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetEvolutionChainUseCase(
    private val repository: PokemonRepository,
    dispatcher: CoroutineDispatcher,
) : FlowUseCase<GetEvolutionChainUseCase.Params, EvolutionChain>(dispatcher) {

    override suspend fun performAction(param: Params?): Flow<Result<EvolutionChain>> =
        param?.let { params ->
            repository.getEvolutionChain(params.pokemonId).map { Result.fromNullable(it) }
        } ?: throw IllegalArgumentException("Params must not be null")

    data class Params(val pokemonId: Int)
}
