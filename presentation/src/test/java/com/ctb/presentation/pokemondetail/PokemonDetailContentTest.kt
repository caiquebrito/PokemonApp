package com.ctb.presentation.pokemondetail

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.ctb.design.compose.theme.PokemonTheme
import com.ctb.domain.models.PokemonDetail
import com.ctb.domain.models.PokemonMove
import com.ctb.domain.models.PokemonStat
import com.ctb.domain.models.PokemonType
import com.ctb.presentation.pokemondetail.viewmodel.PokemonDetailState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * Robolectric-backed Compose UI test: runs on the JVM (no emulator) so it is collected by
 * `testDebugUnitTest` and counts toward jacoco coverage. Serves as the template for future
 * Composable tests in this module.
 */
@RunWith(RobolectricTestRunner::class)
class PokemonDetailContentTest {
    @get:Rule
    val composeRule = createComposeRule()

    private val blaziken =
        PokemonDetail(
            id = 257,
            name = "Blaziken",
            imageUrl = "",
            types = listOf(PokemonType.FIRE, PokemonType.FIGHTING),
            heightDecimetres = 19,
            weightHectograms = 520,
            stats =
            listOf(
                PokemonStat("hp", 80),
                PokemonStat("attack", 120),
            ),
            moves = listOf(PokemonMove("blaze-kick", 36)),
        )

    @Test
    fun rendersNameTypesAndMoves() {
        composeRule.setContent {
            PokemonTheme {
                PokemonDetailContent(
                    state = PokemonDetailState(isLoading = false, detail = blaziken),
                    onBackClick = {},
                    onTypeClick = {},
                )
            }
        }

        composeRule.onNodeWithText("#257 Blaziken").assertIsDisplayed()
        composeRule.onNodeWithText("Fire").assertIsDisplayed()
        // The moves section is lower in the scrollable card, so assert it is composed (exists)
        // rather than currently on-screen.
        composeRule.onNodeWithText("Blaze Kick").assertExists()
    }
}
