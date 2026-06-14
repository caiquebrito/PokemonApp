package com.ctb.presentation.pokemonhome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ctb.design.compose.component.PokemonImage
import com.ctb.design.compose.theme.BorderRadius
import com.ctb.design.compose.theme.PokemonTheme
import com.ctb.design.compose.theme.PokemonTypeColor
import com.ctb.design.compose.theme.Spacing
import com.ctb.domain.models.Pokemon
import com.ctb.domain.models.PokemonType
import com.ctb.presentation.R
import com.ctb.design.R as DesignR

private const val GRID_COLUMNS = 2
private const val PREFETCH_DISTANCE = 4

@Composable
fun PokemonHomeContent(
    isLoading: Boolean,
    isPaginating: Boolean,
    isSearching: Boolean,
    query: String,
    pokemons: List<Pokemon>,
    paginationEnabled: Boolean,
    onQueryChanged: (String) -> Unit,
    onSearchSubmit: () -> Unit,
    onLoadMore: () -> Unit,
    onItemClicked: (Pokemon) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding: PaddingValues ->
        Column(
            modifier =
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = Spacing.small),
        ) {
            Box(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = Spacing.small),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(id = DesignR.drawable.pokemon_logo),
                    contentDescription = stringResource(id = R.string.description_app_logo),
                    modifier = Modifier.size(96.dp),
                )
            }

            OutlinedTextField(
                value = query,
                onValueChange = onQueryChanged,
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = Spacing.small),
                shape = BorderRadius.LARGE,
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onSearchSubmit() }),
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.search_placeholder),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
                colors =
                OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.surface,
                    cursorColor = MaterialTheme.colorScheme.primary,
                ),
            )

            when {
                (isLoading || isSearching) && pokemons.isEmpty() ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }

                pokemons.isEmpty() && query.isNotBlank() ->
                    SearchEmptyState(query = query)

                else ->
                    PokemonGrid(
                        pokemons = pokemons,
                        isPaginating = isPaginating,
                        paginationEnabled = paginationEnabled,
                        onLoadMore = onLoadMore,
                        onItemClicked = onItemClicked,
                    )
            }
        }
    }
}

@Composable
private fun PokemonGrid(
    pokemons: List<Pokemon>,
    isPaginating: Boolean,
    paginationEnabled: Boolean,
    onLoadMore: () -> Unit,
    onItemClicked: (Pokemon) -> Unit,
) {
    val gridState = rememberLazyGridState()

    val shouldLoadMore by remember(paginationEnabled) {
        derivedStateOf {
            if (!paginationEnabled) return@derivedStateOf false
            val lastVisible = gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val total = gridState.layoutInfo.totalItemsCount
            total > 0 && lastVisible >= total - PREFETCH_DISTANCE
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) onLoadMore()
    }

    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Fixed(GRID_COLUMNS),
        horizontalArrangement = Arrangement.spacedBy(Spacing.small),
        verticalArrangement = Arrangement.spacedBy(Spacing.small),
        contentPadding = PaddingValues(bottom = Spacing.small),
        modifier = Modifier.fillMaxSize(),
    ) {
        items(items = pokemons, key = { it.id }) { pokemon ->
            PokemonCard(pokemon = pokemon, onClick = { onItemClicked(pokemon) })
        }

        if (isPaginating) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Box(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = Spacing.small),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
private fun SearchEmptyState(query: String) {
    Column(
        modifier =
        Modifier
            .fillMaxSize()
            .padding(Spacing.large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(id = R.string.search_empty_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = stringResource(id = R.string.search_empty_subtitle, query),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Composable
private fun PokemonCard(
    pokemon: Pokemon,
    onClick: () -> Unit,
) {
    Column(
        modifier =
        Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .shadow(elevation = 4.dp, shape = BorderRadius.MEDIUM)
            .clip(BorderRadius.MEDIUM)
            .background(PokemonTypeColor.cardGradient(pokemon.primaryType))
            .clickable { onClick() }
            .padding(Spacing.xSmall),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        PokemonImage(
            imageUrl = pokemon.imageUrl,
            contentDescription = pokemon.name,
            modifier =
            Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(Spacing.xSmall),
        )
        Text(
            text = pokemon.name,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = Spacing.xSmall),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PokemonHomeContentPreview() {
    val sample =
        listOf(
            Pokemon(1, "Bulbasaur", "", PokemonType.GRASS),
            Pokemon(4, "Charmander", "", PokemonType.FIRE),
            Pokemon(7, "Squirtle", "", PokemonType.WATER),
            Pokemon(25, "Pikachu", "", PokemonType.ELECTRIC),
        )
    PokemonTheme {
        PokemonHomeContent(
            isLoading = false,
            isPaginating = false,
            isSearching = false,
            query = "",
            pokemons = sample,
            paginationEnabled = true,
            onQueryChanged = {},
            onSearchSubmit = {},
            onLoadMore = {},
            onItemClicked = {},
        )
    }
}
