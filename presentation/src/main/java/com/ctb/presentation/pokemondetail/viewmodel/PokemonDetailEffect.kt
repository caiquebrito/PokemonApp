package com.ctb.presentation.pokemondetail.viewmodel

import com.ctb.common.viewmodel.flow.UIEffect

sealed class PokemonDetailEffect : UIEffect {
    object CloseScreen : PokemonDetailEffect()

    data class ShowError(
        val message: String,
    ) : PokemonDetailEffect()
}
