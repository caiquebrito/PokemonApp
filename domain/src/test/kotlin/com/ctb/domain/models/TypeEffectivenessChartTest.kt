package com.ctb.domain.models

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TypeEffectivenessChartTest {

    @Test
    fun `bug type attack profile matches the canonical type chart`() {
        val effectiveness = TypeEffectivenessChart.effectivenessFor(PokemonType.BUG)

        assertEquals(
            listOf(PokemonType.GRASS, PokemonType.PSYCHIC, PokemonType.DARK),
            effectiveness.superEffectiveAgainst,
        )
    }

    @Test
    fun `fire weaknesses are derived by inverting the attack chart`() {
        val effectiveness = TypeEffectivenessChart.effectivenessFor(PokemonType.FIRE)

        assertEquals(
            listOf(PokemonType.WATER, PokemonType.GROUND, PokemonType.ROCK),
            effectiveness.weaknesses,
        )
    }

    @Test
    fun `ghost is immune to normal and fighting`() {
        val effectiveness = TypeEffectivenessChart.effectivenessFor(PokemonType.GHOST)

        assertTrue(effectiveness.immunities.containsAll(listOf(PokemonType.NORMAL, PokemonType.FIGHTING)))
    }

    @Test
    fun `steel is immune to poison`() {
        val effectiveness = TypeEffectivenessChart.effectivenessFor(PokemonType.STEEL)

        assertEquals(listOf(PokemonType.POISON), effectiveness.immunities)
    }

    @Test
    fun `unknown type has no matchups`() {
        val effectiveness = TypeEffectivenessChart.effectivenessFor(PokemonType.UNKNOWN)

        assertEquals(TypeEffectiveness::class, effectiveness::class)
        assertTrue(effectiveness.superEffectiveAgainst.isEmpty())
        assertTrue(effectiveness.notVeryEffectiveAgainst.isEmpty())
        assertTrue(effectiveness.noEffectAgainst.isEmpty())
        assertTrue(effectiveness.weaknesses.isEmpty())
        assertTrue(effectiveness.resistances.isEmpty())
        assertTrue(effectiveness.immunities.isEmpty())
    }
}
