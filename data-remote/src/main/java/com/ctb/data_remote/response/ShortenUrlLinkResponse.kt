package com.ctb.data_remote.response

import com.ctb.domain.models.ShortenUrlLink
import com.google.gson.annotations.SerializedName

data class ShortenUrlLinkResponse(
    @SerializedName("self")
    val self: String,
    @SerializedName("short")
    val short: String,
)

fun ShortenUrlLinkResponse.toDomain() =
    ShortenUrlLink(
        self = self,
        short = short,
    )
