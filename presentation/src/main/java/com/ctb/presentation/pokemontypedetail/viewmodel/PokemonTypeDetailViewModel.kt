package com.ctb.presentation.pokemontypedetail.viewmodel

import com.ctb.common.viewmodel.flow.ViewModel
import com.ctb.domain.models.PokemonType
import com.ctb.domain.models.TypeEffectivenessChart

class PokemonTypeDetailViewModel :
    ViewModel<PokemonTypeDetailState, PokemonTypeDetailEffect>(PokemonTypeDetailState()) {

    fun onCreate(type: PokemonType) {
        if (state.value.effectiveness != null) return
        setState {
            it.copy(
                type = type,
                effectiveness = TypeEffectivenessChart.effectivenessFor(type),
            )
        }
    }

    fun onBack() {
        sendEffect(PokemonTypeDetailEffect.CloseScreen)
    }
}
