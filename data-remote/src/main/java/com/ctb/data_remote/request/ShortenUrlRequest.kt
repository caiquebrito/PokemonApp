package com.ctb.data_remote.request

import com.google.gson.annotations.SerializedName

data class ShortenUrlRequest(
    @SerializedName("url") val url: String,
)

fun String.toShortenUrlRequest() = ShortenUrlRequest(url = this)
