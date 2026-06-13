package com.ctb.domain.usecase

import com.ctb.commonkotlin.usecases.FlowUseCase
import com.ctb.commonkotlin.usecases.Result
import com.ctb.domain.models.ShortenUrlAlias
import com.ctb.domain.repositories.ShortenUrlRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ShortenUrlUseCase(
    private val repository: ShortenUrlRepository,
    dispatcher: CoroutineDispatcher,
) : FlowUseCase<ShortenUrlUseCase.ParamShortenerUrl, ShortenUrlAlias>(dispatcher) {

    override suspend fun performAction(param: ParamShortenerUrl?): Flow<Result<ShortenUrlAlias>> =
        param?.let { paramBanners ->
            repository.getShortenUrl(paramBanners.url).map { Result.fromNullable(it) }
        } ?: throw IllegalArgumentException("ParamShortenerUrl must not be null")

    data class ParamShortenerUrl(
        val url: String,
    )
}
