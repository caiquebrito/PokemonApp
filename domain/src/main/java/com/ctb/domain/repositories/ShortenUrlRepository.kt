package com.ctb.domain.repositories

import com.ctb.domain.models.ShortenUrlAlias
import kotlinx.coroutines.flow.Flow

interface ShortenUrlRepository {
    fun getShortenUrl(url: String): Flow<ShortenUrlAlias>
}
