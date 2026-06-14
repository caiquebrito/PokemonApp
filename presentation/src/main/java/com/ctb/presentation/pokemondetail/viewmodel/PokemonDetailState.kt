package com.ctb.presentation.pokemondetail.viewmodel

import com.ctb.common.viewmodel.flow.UIState
import com.ctb.domain.models.PokemonDetail

data class PokemonDetailState(
    val isLoading: Boolean = true,
    val detail: PokemonDetail? = null,
) : UIState
