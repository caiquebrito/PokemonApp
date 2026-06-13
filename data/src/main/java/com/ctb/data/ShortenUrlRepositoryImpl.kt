package com.ctb.data

import com.ctb.domain.repositories.ShortenUrlRepository

class ShortenUrlRepositoryImpl(
    private val remoteDataSource: ShortenUrlRemoteDataSource,
) : ShortenUrlRepository {
    override fun getShortenUrl(url: String) = remoteDataSource.getShortenUrl(url)
}
