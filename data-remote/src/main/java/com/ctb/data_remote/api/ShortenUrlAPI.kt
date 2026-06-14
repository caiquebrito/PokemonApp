package com.ctb.data_remote.api

import com.ctb.data_remote.request.ShortenUrlRequest
import com.ctb.data_remote.response.ShortenUrlResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ShortenUrlAPI {
    @POST("alias")
    suspend fun shortUrl(
        @Body request: ShortenUrlRequest,
    ): ShortenUrlResponse
}
