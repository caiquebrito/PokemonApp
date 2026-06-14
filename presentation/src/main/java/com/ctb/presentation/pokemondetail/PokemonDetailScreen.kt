package com.ctb.presentation.pokemondetail

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ctb.common.ui.collectAsEffect
import com.ctb.presentation.pokemondetail.viewmodel.PokemonDetailEffect
import com.ctb.presentation.pokemondetail.viewmodel.PokemonDetailViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PokemonDetailScreen(
    pokemonId: Int,
    viewModel: PokemonDetailViewModel = koinViewModel(),
    onBackClick: () -> Unit,
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    viewModel.effect.collectAsEffect { effect ->
        when (effect) {
            PokemonDetailEffect.CloseScreen -> onBackClick.invoke()
            is PokemonDetailEffect.ShowError ->
                Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(key1 = pokemonId) {
        viewModel.onCreate(pokemonId)
    }

    BackHandler {
        viewModel.onBack()
    }

    PokemonDetailContent(
        state = state,
        onBackClick = viewModel::onBack,
    )
}
