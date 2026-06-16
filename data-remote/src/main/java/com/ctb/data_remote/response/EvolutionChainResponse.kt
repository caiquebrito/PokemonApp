package com.ctb.data_remote.response

import com.ctb.domain.models.EvolutionChain
import com.ctb.domain.models.EvolutionStep
import com.google.gson.annotations.SerializedName

private const val ARTWORK_URL_TEMPLATE =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/%d.png"

data class PokemonSpeciesResponse(
    @SerializedName("evolution_chain")
    val evolutionChain: EvolutionChainUrlResponse,
)

data class EvolutionChainUrlResponse(
    @SerializedName("url")
    val url: String,
) {
    fun extractId(): Int = url.trimEnd('/').substringAfterLast('/').toIntOrNull() ?: 0
}

data class EvolutionChainResponse(
    @SerializedName("chain")
    val chain: ChainLinkResponse,
)

data class ChainLinkResponse(
    @SerializedName("species")
    val species: NamedUrlResponse,
    @SerializedName("evolves_to")
    val evolvesTo: List<ChainLinkResponse> = emptyList(),
)

data class NamedUrlResponse(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String,
) {
    fun extractId(): Int = url.trimEnd('/').substringAfterLast('/').toIntOrNull() ?: 0
}

fun EvolutionChainResponse.toDomain(): EvolutionChain {
    val steps = mutableListOf<EvolutionStep>()
    fun flatten(link: ChainLinkResponse) {
        val id = link.species.extractId()
        steps += EvolutionStep(
            id = id,
            name = link.species.name.replaceFirstChar { it.uppercase() },
            imageUrl = ARTWORK_URL_TEMPLATE.format(id),
        )
        link.evolvesTo.forEach { flatten(it) }
    }
    flatten(chain)
    return EvolutionChain(steps = steps)
}
