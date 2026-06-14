package com.ctb.data_remote.response

import com.ctb.domain.models.Pokemon
import com.ctb.domain.models.PokemonDetail
import com.ctb.domain.models.PokemonMove
import com.ctb.domain.models.PokemonStat
import com.ctb.domain.models.PokemonType
import com.google.gson.annotations.SerializedName

data class PokemonDetailResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("height")
    val height: Int,
    @SerializedName("weight")
    val weight: Int,
    @SerializedName("types")
    val types: List<PokemonTypeSlotResponse>,
    @SerializedName("sprites")
    val sprites: PokemonSpritesResponse,
    @SerializedName("stats")
    val stats: List<PokemonStatResponse>,
    @SerializedName("moves")
    val moves: List<PokemonMoveResponse> = emptyList(),
)

data class PokemonMoveResponse(
    @SerializedName("move")
    val move: NamedResponse,
    @SerializedName("version_group_details")
    val versionGroupDetails: List<MoveVersionDetailResponse> = emptyList(),
)

data class MoveVersionDetailResponse(
    @SerializedName("level_learned_at")
    val levelLearnedAt: Int,
    @SerializedName("move_learn_method")
    val moveLearnMethod: NamedResponse,
)

data class PokemonTypeSlotResponse(
    @SerializedName("slot")
    val slot: Int,
    @SerializedName("type")
    val type: NamedResponse,
)

data class PokemonStatResponse(
    @SerializedName("base_stat")
    val baseStat: Int,
    @SerializedName("stat")
    val stat: NamedResponse,
)

data class NamedResponse(
    @SerializedName("name")
    val name: String,
)

data class PokemonSpritesResponse(
    @SerializedName("front_default")
    val frontDefault: String?,
    @SerializedName("other")
    val other: OtherSpritesResponse?,
)

data class OtherSpritesResponse(
    @SerializedName("official-artwork")
    val officialArtwork: OfficialArtworkResponse?,
)

data class OfficialArtworkResponse(
    @SerializedName("front_default")
    val frontDefault: String?,
)

private const val ARTWORK_URL_TEMPLATE =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/%d.png"

/** Best available artwork: official artwork, then default sprite, then a deterministic fallback url. */
fun PokemonDetailResponse.bestImageUrl(): String =
    sprites.other?.officialArtwork?.frontDefault
        ?: sprites.frontDefault
        ?: ARTWORK_URL_TEMPLATE.format(id)

private const val LEVEL_UP_METHOD = "level-up"

private fun PokemonDetailResponse.orderedTypes(): List<PokemonType> =
    types
        .sortedBy { it.slot }
        .map { PokemonType.fromApiName(it.type.name) }

/**
 * The "main" moveset: moves learned by leveling up, deduplicated to the earliest level they
 * become available, ordered by that level. TM/tutor/egg moves are intentionally excluded.
 */
private fun PokemonDetailResponse.mainMoves(): List<PokemonMove> =
    moves
        .mapNotNull { moveResponse ->
            moveResponse.versionGroupDetails
                .filter { it.moveLearnMethod.name == LEVEL_UP_METHOD }
                .minOfOrNull { it.levelLearnedAt }
                ?.let { level -> PokemonMove(name = moveResponse.move.name, levelLearnedAt = level) }
        }
        .sortedWith(compareBy({ it.levelLearnedAt }, { it.name }))

/** Summary used by the Home grid. */
fun PokemonDetailResponse.toPokemon(): Pokemon {
    val ordered = orderedTypes()
    return Pokemon(
        id = id,
        name = name.replaceFirstChar { it.uppercase() },
        imageUrl = bestImageUrl(),
        primaryType = ordered.firstOrNull() ?: PokemonType.UNKNOWN,
        secondaryType = ordered.getOrNull(1),
    )
}

/** Full model used by the Detail screen. */
fun PokemonDetailResponse.toDomain(): PokemonDetail =
    PokemonDetail(
        id = id,
        name = name.replaceFirstChar { it.uppercase() },
        imageUrl = bestImageUrl(),
        types = orderedTypes(),
        heightDecimetres = height,
        weightHectograms = weight,
        stats =
        stats.map {
            PokemonStat(
                name = it.stat.name,
                baseValue = it.baseStat,
            )
        },
        moves = mainMoves(),
    )
