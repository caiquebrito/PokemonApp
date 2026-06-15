package com.ctb.presentation.pokemontypedetail

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ctb.common.ui.collectAsEffect
import com.ctb.domain.models.PokemonType
import com.ctb.presentation.pokemontypedetail.viewmodel.PokemonTypeDetailEffect
import com.ctb.presentation.pokemontypedetail.viewmodel.PokemonTypeDetailViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PokemonTypeDetailScreen(
    type: PokemonType,
    viewModel: PokemonTypeDetailViewModel = koinViewModel(),
    onBackClick: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    viewModel.effect.collectAsEffect { effect ->
        when (effect) {
            PokemonTypeDetailEffect.CloseScreen -> onBackClick.invoke()
        }
    }

    LaunchedEffect(key1 = type) {
        viewModel.onCreate(type)
    }

    BackHandler {
        viewModel.onBack()
    }

    PokemonTypeDetailContent(
        state = state,
        onBackClick = viewModel::onBack,
    )
}
