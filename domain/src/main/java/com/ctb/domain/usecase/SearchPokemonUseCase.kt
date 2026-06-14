package com.ctb.domain.usecase

import com.ctb.commonkotlin.usecases.FlowUseCase
import com.ctb.commonkotlin.usecases.Result
import com.ctb.domain.models.Pokemon
import com.ctb.domain.repositories.PokemonRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchPokemonUseCase(
    private val repository: PokemonRepository,
    dispatcher: CoroutineDispatcher,
) : FlowUseCase<SearchPokemonUseCase.Params, Pokemon>(dispatcher) {

    override suspend fun performAction(param: Params?): Flow<Result<Pokemon>> =
        param?.let { params ->
            repository
                .searchPokemonByName(params.name.trim().lowercase())
                .map { Result.fromNullable(it) }
        } ?: throw IllegalArgumentException("Params must not be null")

    data class Params(
        val name: String,
    )
}
