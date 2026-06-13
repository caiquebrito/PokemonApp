package com.ctb.data_remote.response

import com.ctb.domain.models.ShortenUrlAlias
import com.google.gson.annotations.SerializedName

data class ShortenUrlResponse(
    @SerializedName("alias")
    val alias: String,
    @SerializedName("_links")
    val links: ShortenUrlLinkResponse,
)

fun ShortenUrlResponse.toDomain() =
    ShortenUrlAlias(
        alias = alias,
        links = links.toDomain(),
    )
