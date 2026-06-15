package com.ctb.domain.models

import com.ctb.domain.models.PokemonType.BUG
import com.ctb.domain.models.PokemonType.DARK
import com.ctb.domain.models.PokemonType.DRAGON
import com.ctb.domain.models.PokemonType.ELECTRIC
import com.ctb.domain.models.PokemonType.FAIRY
import com.ctb.domain.models.PokemonType.FIGHTING
import com.ctb.domain.models.PokemonType.FIRE
import com.ctb.domain.models.PokemonType.FLYING
import com.ctb.domain.models.PokemonType.GHOST
import com.ctb.domain.models.PokemonType.GRASS
import com.ctb.domain.models.PokemonType.GROUND
import com.ctb.domain.models.PokemonType.ICE
import com.ctb.domain.models.PokemonType.NORMAL
import com.ctb.domain.models.PokemonType.POISON
import com.ctb.domain.models.PokemonType.PSYCHIC
import com.ctb.domain.models.PokemonType.ROCK
import com.ctb.domain.models.PokemonType.STEEL
import com.ctb.domain.models.PokemonType.WATER

/**
 * Static Pokémon type effectiveness chart (Gen 6+, includes Fairy; unchanged since). For each
 * type, [attackProfiles] lists which other types it deals 2x/0.5x/0x damage to when attacking.
 * [effectivenessFor] derives the defensive side (weaknesses/resistances/immunities) by inverting
 * this table.
 */
object TypeEffectivenessChart {

    fun effectivenessFor(type: PokemonType): TypeEffectiveness {
        val attack = attackProfiles[type] ?: AttackProfile()
        return TypeEffectiveness(
            type = type,
            superEffectiveAgainst = attack.superEffectiveAgainst,
            notVeryEffectiveAgainst = attack.notVeryEffectiveAgainst,
            noEffectAgainst = attack.noEffectAgainst,
            weaknesses = attackersWhere { type in it.superEffectiveAgainst },
            resistances = attackersWhere { type in it.notVeryEffectiveAgainst },
            immunities = attackersWhere { type in it.noEffectAgainst },
        )
    }

    private fun attackersWhere(matches: (AttackProfile) -> Boolean): List<PokemonType> =
        attackProfiles.filterValues(matches).keys.toList()

    private data class AttackProfile(
        val superEffectiveAgainst: List<PokemonType> = emptyList(),
        val notVeryEffectiveAgainst: List<PokemonType> = emptyList(),
        val noEffectAgainst: List<PokemonType> = emptyList(),
    )

    private val attackProfiles: Map<PokemonType, AttackProfile> =
        mapOf(
            NORMAL to
                AttackProfile(
                    notVeryEffectiveAgainst = listOf(ROCK, STEEL),
                    noEffectAgainst = listOf(GHOST),
                ),
            FIRE to
                AttackProfile(
                    superEffectiveAgainst = listOf(GRASS, ICE, BUG, STEEL),
                    notVeryEffectiveAgainst = listOf(FIRE, WATER, ROCK, DRAGON),
                ),
            WATER to
                AttackProfile(
                    superEffectiveAgainst = listOf(FIRE, GROUND, ROCK),
                    notVeryEffectiveAgainst = listOf(WATER, GRASS, DRAGON),
                ),
            ELECTRIC to
                AttackProfile(
                    superEffectiveAgainst = listOf(WATER, FLYING),
                    notVeryEffectiveAgainst = listOf(ELECTRIC, GRASS, DRAGON),
                    noEffectAgainst = listOf(GROUND),
                ),
            GRASS to
                AttackProfile(
                    superEffectiveAgainst = listOf(WATER, GROUND, ROCK),
                    notVeryEffectiveAgainst = listOf(FIRE, GRASS, POISON, FLYING, BUG, DRAGON, STEEL),
                ),
            ICE to
                AttackProfile(
                    superEffectiveAgainst = listOf(GRASS, GROUND, FLYING, DRAGON),
                    notVeryEffectiveAgainst = listOf(FIRE, WATER, ICE, STEEL),
                ),
            FIGHTING to
                AttackProfile(
                    superEffectiveAgainst = listOf(NORMAL, ICE, ROCK, DARK, STEEL),
                    notVeryEffectiveAgainst = listOf(POISON, FLYING, PSYCHIC, BUG, FAIRY),
                    noEffectAgainst = listOf(GHOST),
                ),
            POISON to
                AttackProfile(
                    superEffectiveAgainst = listOf(GRASS, FAIRY),
                    notVeryEffectiveAgainst = listOf(POISON, GROUND, ROCK, GHOST),
                    noEffectAgainst = listOf(STEEL),
                ),
            GROUND to
                AttackProfile(
                    superEffectiveAgainst = listOf(FIRE, ELECTRIC, POISON, ROCK, STEEL),
                    notVeryEffectiveAgainst = listOf(GRASS, BUG),
                    noEffectAgainst = listOf(FLYING),
                ),
            FLYING to
                AttackProfile(
                    superEffectiveAgainst = listOf(GRASS, FIGHTING, BUG),
                    notVeryEffectiveAgainst = listOf(ELECTRIC, ROCK, STEEL),
                ),
            PSYCHIC to
                AttackProfile(
                    superEffectiveAgainst = listOf(FIGHTING, POISON),
                    notVeryEffectiveAgainst = listOf(PSYCHIC, STEEL),
                    noEffectAgainst = listOf(DARK),
                ),
            BUG to
                AttackProfile(
                    superEffectiveAgainst = listOf(GRASS, PSYCHIC, DARK),
                    notVeryEffectiveAgainst = listOf(FIRE, FIGHTING, POISON, FLYING, GHOST, STEEL, FAIRY),
                ),
            ROCK to
                AttackProfile(
                    superEffectiveAgainst = listOf(FIRE, ICE, FLYING, BUG),
                    notVeryEffectiveAgainst = listOf(FIGHTING, GROUND, STEEL),
                ),
            GHOST to
                AttackProfile(
                    superEffectiveAgainst = listOf(PSYCHIC, GHOST),
                    notVeryEffectiveAgainst = listOf(DARK),
                    noEffectAgainst = listOf(NORMAL),
                ),
            DRAGON to
                AttackProfile(
                    superEffectiveAgainst = listOf(DRAGON),
                    notVeryEffectiveAgainst = listOf(STEEL),
                    noEffectAgainst = listOf(FAIRY),
                ),
            DARK to
                AttackProfile(
                    superEffectiveAgainst = listOf(PSYCHIC, GHOST),
                    notVeryEffectiveAgainst = listOf(FIGHTING, DARK, FAIRY),
                ),
            STEEL to
                AttackProfile(
                    superEffectiveAgainst = listOf(ICE, ROCK),
                    notVeryEffectiveAgainst = listOf(FIRE, WATER, ELECTRIC, STEEL),
                ),
            FAIRY to
                AttackProfile(
                    superEffectiveAgainst = listOf(FIGHTING, DRAGON, DARK),
                    notVeryEffectiveAgainst = listOf(FIRE, POISON, STEEL),
                ),
        )
}
