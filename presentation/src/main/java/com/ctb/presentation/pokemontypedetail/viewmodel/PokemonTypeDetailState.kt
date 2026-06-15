package com.ctb.presentation.pokemontypedetail.viewmodel

import com.ctb.common.viewmodel.flow.UIState
import com.ctb.domain.models.PokemonType
import com.ctb.domain.models.TypeEffectiveness

data class PokemonTypeDetailState(
    val type: PokemonType = PokemonType.UNKNOWN,
    val effectiveness: TypeEffectiveness? = null,
) : UIState
