package com.ctb.presentation.pokemondetail

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
data class PokemonDetailRoute(
    val id: Int,
)

fun NavController.openPokemonDetail(id: Int) {
    navigate(PokemonDetailRoute(id = id))
}

fun NavGraphBuilder.pokemonDetail(onBackClick: () -> Unit) =
    composable<PokemonDetailRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<PokemonDetailRoute>()
        PokemonDetailScreen(
            pokemonId = route.id,
            onBackClick = onBackClick,
        )
    }
