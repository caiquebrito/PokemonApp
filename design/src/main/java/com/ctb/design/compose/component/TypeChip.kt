package com.ctb.design.compose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import com.ctb.design.compose.theme.PokemonTypeColor
import com.ctb.design.compose.theme.Spacing
import com.ctb.domain.models.PokemonType

/** A pill-shaped label for a Pokémon type, colored via [PokemonTypeColor]. Optionally clickable. */
@Composable
fun TypeChip(
    type: PokemonType,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    val clickModifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier

    Box(
        modifier =
        modifier
            .clip(CircleShape)
            .background(PokemonTypeColor.colorFor(type))
            .then(clickModifier)
            .padding(horizontal = Spacing.large, vertical = Spacing.xSmall),
    ) {
        Text(
            text = type.apiName.replaceFirstChar { it.uppercase() },
            color = PokemonTypeColor.onTypeColor,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}
