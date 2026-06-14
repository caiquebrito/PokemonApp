package com.ctb.presentation.pokemonhome

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ctb.common.ui.collectAsEffect
import com.ctb.domain.models.Pokemon
import com.ctb.presentation.pokemonhome.viewmodel.PokemonHomeEffect
import com.ctb.presentation.pokemonhome.viewmodel.PokemonHomeViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PokemonHomeScreen(
    viewModel: PokemonHomeViewModel = koinViewModel(),
    onItemClicked: (Pokemon) -> Unit,
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    viewModel.effect.collectAsEffect { effect ->
        when (effect) {
            is PokemonHomeEffect.ShowError ->
                Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
        }
    }

    PokemonHomeContent(
        isLoading = state.isLoading,
        isPaginating = state.isPaginating,
        isSearching = state.isSearching,
        query = state.query,
        pokemons = state.visiblePokemons,
        paginationEnabled = state.query.isBlank(),
        onQueryChanged = viewModel::onQueryChanged,
        onSearchSubmit = viewModel::onSearchSubmit,
        onLoadMore = viewModel::loadNextPage,
        onItemClicked = onItemClicked,
    )

    LaunchedEffect(key1 = Unit) {
        viewModel.onCreate()
    }
}
