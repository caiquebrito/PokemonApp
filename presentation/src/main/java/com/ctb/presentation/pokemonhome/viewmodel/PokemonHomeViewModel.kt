package com.ctb.presentation.pokemonhome.viewmodel

import androidx.lifecycle.viewModelScope
import com.ctb.common.ui.ResourceProvider
import com.ctb.common.viewmodel.flow.ViewModel
import com.ctb.commonkotlin.usecases.onFailure
import com.ctb.commonkotlin.usecases.onSuccess
import com.ctb.domain.usecase.GetPokemonPageUseCase
import com.ctb.domain.usecase.SearchPokemonUseCase
import com.ctb.presentation.R
import kotlinx.coroutines.launch

class PokemonHomeViewModel(
    private val getPokemonPageUseCase: GetPokemonPageUseCase,
    private val searchPokemonUseCase: SearchPokemonUseCase,
    private val resourceProvider: ResourceProvider,
) : ViewModel<PokemonHomeState, PokemonHomeEffect>(PokemonHomeState()) {

    fun onCreate() {
        if (state.value.pokemons.isEmpty()) {
            loadFirstPage()
        }
    }

    private fun loadFirstPage() {
        viewModelScope.launch {
            setState { it.copy(isLoading = true) }
            loadPage(offset = 0)
            setState { it.copy(isLoading = false) }
        }
    }

    fun loadNextPage() {
        val current = state.value
        if (current.isLoading || current.isPaginating || !current.hasMore) return
        val offset = current.nextOffset ?: return
        viewModelScope.launch {
            setState { it.copy(isPaginating = true) }
            loadPage(offset)
            setState { it.copy(isPaginating = false) }
        }
    }

    private suspend fun loadPage(offset: Int) {
        getPokemonPageUseCase(
            GetPokemonPageUseCase.Params(limit = PAGE_SIZE, offset = offset),
        ).collect { result ->
            result.onSuccess { page ->
                setState {
                    it.copy(
                        pokemons = (it.pokemons + page.items).distinctBy { pokemon -> pokemon.id },
                        nextOffset = page.nextOffset,
                        hasMore = page.hasMore,
                    )
                }
            }
            result.onFailure { throwable ->
                sendEffect(
                    PokemonHomeEffect.ShowError(
                        message =
                        throwable.message
                            ?: resourceProvider.getString(resId = R.string.error_generic),
                    ),
                )
            }
        }
    }

    fun onQueryChanged(newQuery: String) {
        setState { it.copy(query = newQuery) }
    }

    /**
     * Triggered when the user submits the search. If the name is already in the loaded grid we do
     * nothing (the live filter already shows it). Otherwise we look it up on the API and either
     * append it to the list or report that no such Pokémon exists.
     */
    fun onSearchSubmit() {
        val query = state.value.query.trim()
        if (query.isEmpty() || state.value.isSearching) return

        val alreadyLoaded =
            state.value.pokemons.any { it.name.equals(query, ignoreCase = true) }
        if (alreadyLoaded) return

        viewModelScope.launch {
            setState { it.copy(isSearching = true) }
            searchPokemonUseCase(SearchPokemonUseCase.Params(name = query)).collect { result ->
                result.onSuccess { pokemon ->
                    setState {
                        it.copy(
                            pokemons = (it.pokemons + pokemon).distinctBy { item -> item.id },
                        )
                    }
                }
                result.onFailure {
                    sendEffect(
                        PokemonHomeEffect.ShowError(
                            message =
                            resourceProvider.getString(
                                resId = R.string.error_pokemon_not_found,
                                query,
                            ),
                        ),
                    )
                }
            }
            setState { it.copy(isSearching = false) }
        }
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}
