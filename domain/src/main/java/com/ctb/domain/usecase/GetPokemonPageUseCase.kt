package com.ctb.domain.usecase

import com.ctb.commonkotlin.usecases.FlowUseCase
import com.ctb.commonkotlin.usecases.Result
import com.ctb.domain.models.PokemonPage
import com.ctb.domain.repositories.PokemonRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetPokemonPageUseCase(
    private val repository: PokemonRepository,
    dispatcher: CoroutineDispatcher,
) : FlowUseCase<GetPokemonPageUseCase.Params, PokemonPage>(dispatcher) {

    override suspend fun performAction(param: Params?): Flow<Result<PokemonPage>> =
        param?.let { params ->
            repository
                .getPokemonPage(limit = params.limit, offset = params.offset)
                .map { Result.fromNullable(it) }
        } ?: throw IllegalArgumentException("Params must not be null")

    data class Params(
        val limit: Int,
        val offset: Int,
    )
}
