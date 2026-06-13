package com.ctb.data

import com.ctb.domain.models.ShortenUrlAlias
import kotlinx.coroutines.flow.Flow

interface ShortenUrlRemoteDataSource {
    fun getShortenUrl(url: String): Flow<ShortenUrlAlias>
}
