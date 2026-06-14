package com.ctb.domain.usecase

import com.ctb.commonkotlin.usecases.FlowUseCase
import com.ctb.commonkotlin.usecases.Result
import com.ctb.domain.models.PokemonDetail
import com.ctb.domain.repositories.PokemonRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetPokemonDetailUseCase(
    private val repository: PokemonRepository,
    dispatcher: CoroutineDispatcher,
) : FlowUseCase<GetPokemonDetailUseCase.Params, PokemonDetail>(dispatcher) {

    override suspend fun performAction(param: Params?): Flow<Result<PokemonDetail>> =
        param?.let { params ->
            repository.getPokemonDetail(params.id).map { Result.fromNullable(it) }
        } ?: throw IllegalArgumentException("Params must not be null")

    data class Params(
        val id: Int,
    )
}
