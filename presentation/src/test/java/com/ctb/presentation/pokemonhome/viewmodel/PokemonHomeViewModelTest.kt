package com.ctb.presentation.pokemonhome.viewmodel

import com.ctb.common.ui.ResourceProvider
import com.ctb.commonkotlin.test.collectTestFlows
import com.ctb.commonkotlin.test.mockFailure
import com.ctb.commonkotlin.test.mockSuccess
import com.ctb.commonkotlin.usecases.CoroutinesTestRule
import com.ctb.domain.models.Pokemon
import com.ctb.domain.models.PokemonPage
import com.ctb.domain.models.PokemonType
import com.ctb.domain.usecase.GetPokemonPageUseCase
import com.ctb.domain.usecase.SearchPokemonUseCase
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PokemonHomeViewModelTest {
    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private val getPokemonPageUseCase: GetPokemonPageUseCase = mockk()
    private val searchPokemonUseCase: SearchPokemonUseCase = mockk()
    private val resourceProvider: ResourceProvider = mockk(relaxed = true)
    private lateinit var viewModel: PokemonHomeViewModel

    private val bulbasaur = Pokemon(1, "Bulbasaur", "", PokemonType.GRASS)
    private val charmander = Pokemon(4, "Charmander", "", PokemonType.FIRE)
    private val dialga = Pokemon(483, "Dialga", "", PokemonType.STEEL)

    @Before
    fun setup() {
        viewModel = PokemonHomeViewModel(getPokemonPageUseCase, searchPokemonUseCase, resourceProvider)
    }

    @Test
    fun `onCreate loads the first page and clears loading`() =
        runTest {
            getPokemonPageUseCase.mockSuccess(
                PokemonPage(items = listOf(bulbasaur, charmander), nextOffset = 20, hasMore = true),
            )

            viewModel.onCreate()

            assertEquals(listOf(bulbasaur, charmander), viewModel.state.value.pokemons)
            assertFalse(viewModel.state.value.isLoading)
        }

    @Test
    fun `query filters the visible pokemons by name`() =
        runTest {
            getPokemonPageUseCase.mockSuccess(
                PokemonPage(items = listOf(bulbasaur, charmander), nextOffset = 20, hasMore = true),
            )
            viewModel.onCreate()

            viewModel.onQueryChanged("char")

            assertEquals(listOf(charmander), viewModel.state.value.visiblePokemons)
        }

    @Test
    fun `loadNextPage does nothing when there are no more pages`() =
        runTest {
            getPokemonPageUseCase.mockSuccess(
                PokemonPage(items = listOf(bulbasaur), nextOffset = null, hasMore = false),
            )
            viewModel.onCreate()

            viewModel.loadNextPage()

            assertEquals(listOf(bulbasaur), viewModel.state.value.pokemons)
        }

    @Test
    fun `failure emits a ShowError effect`() =
        runTest {
            getPokemonPageUseCase.mockFailure(RuntimeException("boom"))

            collectTestFlows(viewModel.effect) { effect ->
                viewModel.onCreate()
                effect.assertValuesInOrder(PokemonHomeEffect.ShowError("boom"))
            }
        }

    @Test
    fun `search appends a pokemon that is not in the loaded list`() =
        runTest {
            getPokemonPageUseCase.mockSuccess(
                PokemonPage(items = listOf(bulbasaur, charmander), nextOffset = 20, hasMore = true),
            )
            searchPokemonUseCase.mockSuccess(dialga)
            viewModel.onCreate()

            viewModel.onQueryChanged("dialga")
            viewModel.onSearchSubmit()

            assertTrue(viewModel.state.value.pokemons.contains(dialga))
            assertFalse(viewModel.state.value.isSearching)
        }

    @Test
    fun `search emits ShowError when the pokemon does not exist`() =
        runTest {
            getPokemonPageUseCase.mockSuccess(
                PokemonPage(items = listOf(bulbasaur), nextOffset = 20, hasMore = true),
            )
            searchPokemonUseCase.mockFailure(RuntimeException("404"))
            viewModel.onCreate()

            collectTestFlows(viewModel.effect) { effect ->
                viewModel.onQueryChanged("missingno")
                viewModel.onSearchSubmit()
                effect.assertValuesInOrder(PokemonHomeEffect.ShowError(""))
            }
        }
}
