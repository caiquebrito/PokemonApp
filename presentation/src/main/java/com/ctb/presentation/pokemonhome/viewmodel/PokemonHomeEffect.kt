package com.ctb.presentation.pokemonhome.viewmodel

import com.ctb.common.viewmodel.flow.UIEffect

sealed class PokemonHomeEffect : UIEffect {
    data class ShowError(
        val message: String,
    ) : PokemonHomeEffect()
}
