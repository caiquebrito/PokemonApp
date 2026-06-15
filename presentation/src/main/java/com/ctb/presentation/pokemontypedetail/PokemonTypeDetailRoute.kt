package com.ctb.presentation.pokemontypedetail

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.ctb.domain.models.PokemonType
import kotlinx.serialization.Serializable

@Serializable
data class PokemonTypeDetailRoute(
    val typeName: String,
)

fun NavController.openPokemonTypeDetail(type: PokemonType) {
    navigate(PokemonTypeDetailRoute(typeName = type.apiName))
}

fun NavGraphBuilder.pokemonTypeDetail(onBackClick: () -> Unit) =
    composable<PokemonTypeDetailRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<PokemonTypeDetailRoute>()
        PokemonTypeDetailScreen(
            type = PokemonType.fromApiName(route.typeName),
            onBackClick = onBackClick,
        )
    }
