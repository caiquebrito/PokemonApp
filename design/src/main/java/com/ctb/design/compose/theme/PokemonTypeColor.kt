package com.ctb.design.compose.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import com.ctb.domain.models.PokemonType

/**
 * The single source of truth for the "predominant color" rule: every Pokémon type maps to one
 * color token. A Pokémon's primary type drives the gradient on its Home card and Detail header,
 * so the same token is reused across the whole app.
 */
object PokemonTypeColor {
    private val tokens: Map<PokemonType, Color> =
        mapOf(
            PokemonType.NORMAL to Color(0xFFA8A77A),
            PokemonType.FIRE to Color(0xFFEE8130),
            PokemonType.WATER to Color(0xFF6390F0),
            PokemonType.GRASS to Color(0xFF4E9E61),
            PokemonType.ELECTRIC to Color(0xFFF7D02C),
            PokemonType.ICE to Color(0xFF96D9D6),
            PokemonType.FIGHTING to Color(0xFFC22E28),
            PokemonType.POISON to Color(0xFFA33EA1),
            PokemonType.GROUND to Color(0xFFE2BF65),
            PokemonType.FLYING to Color(0xFFA98FF3),
            PokemonType.PSYCHIC to Color(0xFFF95587),
            PokemonType.BUG to Color(0xFFA6B91A),
            PokemonType.ROCK to Color(0xFFB6A136),
            PokemonType.GHOST to Color(0xFF735797),
            PokemonType.DRAGON to Color(0xFF6F35FC),
            PokemonType.DARK to Color(0xFF705746),
            PokemonType.STEEL to Color(0xFFB7B7CE),
            PokemonType.FAIRY to Color(0xFFD685AD),
            PokemonType.UNKNOWN to Color(0xFF68A090),
        )

    /** Per-stat bar colors, keyed by PokeAPI stat name (hp, attack, defense, …). */
    private val NeutralStatColor = Color(0xFF9E9E9E)

    private val statTokens: Map<String, Color> =
        mapOf(
            "hp" to Color(0xFFF2E14C),
            "attack" to Color(0xFFF0796B),
            "defense" to Color(0xFF7D8FE3),
            "special-attack" to Color(0xFFC58BE8),
            "special-defense" to Color(0xFFF285C6),
            "speed" to Color(0xFF7CCB7E),
        )

    /** Foreground (text/icons) placed on top of a type-colored surface or gradient. */
    val onTypeColor: Color = Palette.White

    /** The flat color token for a type, used for chips and accents. */
    fun colorFor(type: PokemonType): Color = tokens[type] ?: tokens.getValue(PokemonType.UNKNOWN)

    /** Color token for a stat bar; falls back to a neutral gray for unknown stats. */
    fun statColor(statApiName: String): Color =
        statTokens[statApiName.lowercase()] ?: NeutralStatColor

    /**
     * Card gradient: a soft tint of the type color at the top fading into the saturated token at
     * the bottom — matching the reference Pokédex card look.
     */
    fun cardGradient(type: PokemonType): Brush {
        val base = colorFor(type)
        return Brush.verticalGradient(
            colors =
            listOf(
                lerp(base, Color.White, TOP_TINT_FRACTION),
                base,
            ),
        )
    }

    /**
     * Detail screen background: a dark wash at the very top easing down into the saturated type
     * color, so the white info card reads cleanly on top of it (matches the reference detail view).
     */
    fun headerGradient(type: PokemonType): Brush {
        val base = colorFor(type)
        return Brush.verticalGradient(
            colors =
            listOf(
                lerp(base, Color.Black, HEADER_TOP_SHADE_FRACTION),
                base,
            ),
        )
    }

    private const val TOP_TINT_FRACTION = 0.45f
    private const val HEADER_TOP_SHADE_FRACTION = 0.65f
}
