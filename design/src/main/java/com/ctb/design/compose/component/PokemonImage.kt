package com.ctb.design.compose.component

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

/**
 * Thin Coil wrapper used for Pokémon artwork on both the Home grid and the Detail header.
 * Centralizes the crossfade + request config so callers only pass a url and modifier.
 */
@Composable
fun PokemonImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier.size(96.dp),
    contentScale: ContentScale = ContentScale.Fit,
) {
    AsyncImage(
        model =
        ImageRequest
            .Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier,
    )
}
