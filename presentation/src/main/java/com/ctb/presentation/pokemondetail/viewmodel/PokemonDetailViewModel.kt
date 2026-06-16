package com.ctb.presentation.pokemondetail.viewmodel

import androidx.lifecycle.viewModelScope
import com.ctb.common.ui.ResourceProvider
import com.ctb.common.viewmodel.flow.ViewModel
import com.ctb.commonkotlin.usecases.onFailure
import com.ctb.commonkotlin.usecases.onSuccess
import com.ctb.domain.usecase.GetEvolutionChainUseCase
import com.ctb.domain.usecase.GetPokemonDetailUseCase
import com.ctb.presentation.R
import kotlinx.coroutines.launch

class PokemonDetailViewModel(
    private val getPokemonDetailUseCase: GetPokemonDetailUseCase,
    private val getEvolutionChainUseCase: GetEvolutionChainUseCase,
    private val resourceProvider: ResourceProvider,
) : ViewModel<PokemonDetailState, PokemonDetailEffect>(PokemonDetailState()) {

    fun onCreate(pokemonId: Int) {
        if (state.value.detail != null) return
        viewModelScope.launch {
            setState { it.copy(isLoading = true) }
            getPokemonDetailUseCase(
                GetPokemonDetailUseCase.Params(id = pokemonId),
            ).collect { result ->
                result.onSuccess { detail ->
                    setState { it.copy(detail = detail) }
                }
                result.onFailure { throwable ->
                    sendEffect(
                        PokemonDetailEffect.ShowError(
                            message =
                            throwable.message
                                ?: resourceProvider.getString(resId = R.string.error_generic),
                        ),
                    )
                }
            }
            setState { it.copy(isLoading = false) }
        }
        viewModelScope.launch {
            getEvolutionChainUseCase(
                GetEvolutionChainUseCase.Params(pokemonId = pokemonId),
            ).collect { result ->
                result.onSuccess { chain -> setState { it.copy(evolutionChain = chain) } }
            }
        }
    }

    fun onBack() {
        sendEffect(PokemonDetailEffect.CloseScreen)
    }
}
