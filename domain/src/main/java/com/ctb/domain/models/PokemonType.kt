package com.ctb.domain.models

/**
 * The 18 canonical Pokémon types plus an [UNKNOWN] fallback for unexpected API values.
 * The primary type drives the predominant color used for card and detail gradients.
 */
enum class PokemonType(val apiName: String) {
    NORMAL("normal"),
    FIRE("fire"),
    WATER("water"),
    GRASS("grass"),
    ELECTRIC("electric"),
    ICE("ice"),
    FIGHTING("fighting"),
    POISON("poison"),
    GROUND("ground"),
    FLYING("flying"),
    PSYCHIC("psychic"),
    BUG("bug"),
    ROCK("rock"),
    GHOST("ghost"),
    DRAGON("dragon"),
    DARK("dark"),
    STEEL("steel"),
    FAIRY("fairy"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromApiName(apiName: String?): PokemonType =
            entries.firstOrNull { it.apiName == apiName?.lowercase() } ?: UNKNOWN
    }
}
