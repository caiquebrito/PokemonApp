package com.ctb.data_remote.response

import com.google.gson.annotations.SerializedName

data class PokemonListResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String?,
    @SerializedName("previous")
    val previous: String?,
    @SerializedName("results")
    val results: List<PokemonListItemResponse>,
)

data class PokemonListItemResponse(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String,
) {
    /** Extracts the numeric id from a url such as `https://pokeapi.co/api/v2/pokemon/1/`. */
    fun extractId(): Int =
        url.trimEnd('/').substringAfterLast('/').toIntOrNull() ?: 0
}
