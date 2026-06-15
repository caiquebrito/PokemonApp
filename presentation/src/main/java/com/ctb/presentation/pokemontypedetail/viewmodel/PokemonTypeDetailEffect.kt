package com.ctb.presentation.pokemontypedetail.viewmodel

import com.ctb.common.viewmodel.flow.UIEffect

sealed class PokemonTypeDetailEffect : UIEffect {
    object CloseScreen : PokemonTypeDetailEffect()
}
