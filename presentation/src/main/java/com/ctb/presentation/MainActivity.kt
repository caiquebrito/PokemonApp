package com.ctb.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.ctb.design.compose.component.PokemonNavHost
import com.ctb.design.compose.theme.PokemonTheme
import com.ctb.presentation.pokemondetail.openPokemonDetail
import com.ctb.presentation.pokemondetail.pokemonDetail
import com.ctb.presentation.pokemonhome.PokemonHomeRoute
import com.ctb.presentation.pokemonhome.pokemonHome
import com.ctb.presentation.pokemontypedetail.openPokemonTypeDetail
import com.ctb.presentation.pokemontypedetail.pokemonTypeDetail

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokemonTheme {
                val navController = rememberNavController()

                PokemonNavHost(
                    navController = navController,
                    startDestination = PokemonHomeRoute,
                ) {
                    pokemonHome(
                        onItemClicked = { pokemon ->
                            navController.openPokemonDetail(pokemon.id)
                        },
                    )
                    pokemonDetail(
                        onBackClick = { navController.popBackStack() },
                        onTypeClick = { type -> navController.openPokemonTypeDetail(type) },
                    )
                    pokemonTypeDetail(
                        onBackClick = { navController.popBackStack() },
                    )
                }
            }
        }
    }
}
