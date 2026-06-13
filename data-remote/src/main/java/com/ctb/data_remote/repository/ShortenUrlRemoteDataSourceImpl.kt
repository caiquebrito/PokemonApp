package com.ctb.data_remote.repository

import com.ctb.data.ShortenUrlRemoteDataSource
import com.ctb.data_remote.api.ShortenUrlAPI
import com.ctb.data_remote.request.toShortenUrlRequest
import com.ctb.data_remote.response.toDomain
import com.ctb.domain.models.ShortenUrlAlias
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class ShortenUrlRemoteDataSourceImpl(
    val api: ShortenUrlAPI,
) : ShortenUrlRemoteDataSource {
    override fun getShortenUrl(url: String): Flow<ShortenUrlAlias> =
        flow {
            emit(
                api.shortUrl(request = url.toShortenUrlRequest()).toDomain(),
            )
        }.catch {
            // handle customized errorBody if needed
            throw it
        }
}
