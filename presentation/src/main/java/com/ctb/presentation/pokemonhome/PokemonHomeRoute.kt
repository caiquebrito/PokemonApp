package com.ctb.presentation.pokemonhome

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ctb.domain.models.Pokemon
import kotlinx.serialization.Serializable

@Serializable
object PokemonHomeRoute

fun NavController.openPokemonHome() {
    navigate(PokemonHomeRoute)
}

fun NavGraphBuilder.pokemonHome(onItemClicked: (Pokemon) -> Unit) =
    composable<PokemonHomeRoute> {
        PokemonHomeScreen(
            onItemClicked = onItemClicked,
        )
    }
