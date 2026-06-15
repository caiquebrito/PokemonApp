package com.ctb.presentation.pokemontypedetail

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.ctb.design.compose.theme.PokemonTheme
import com.ctb.domain.models.PokemonType
import com.ctb.domain.models.TypeEffectivenessChart
import com.ctb.presentation.pokemontypedetail.viewmodel.PokemonTypeDetailState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PokemonTypeDetailContentTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun rendersTypeNameAndMatchups() {
        composeRule.setContent {
            PokemonTheme {
                PokemonTypeDetailContent(
                    state =
                    PokemonTypeDetailState(
                        type = PokemonType.BUG,
                        effectiveness = TypeEffectivenessChart.effectivenessFor(PokemonType.BUG),
                    ),
                    onBackClick = {},
                )
            }
        }

        composeRule.onNodeWithText("Bug").assertIsDisplayed()
        // Lower sections may scroll off-screen in the test viewport, so assert they're composed
        // rather than currently visible.
        composeRule.onNodeWithText("Strong against").assertExists()
        // Psychic only appears in the "Strong against" section for Bug (unlike Grass, which is
        // both super effective against and resistant to Bug, so it renders twice).
        composeRule.onNodeWithText("Psychic").assertExists()
    }
}
