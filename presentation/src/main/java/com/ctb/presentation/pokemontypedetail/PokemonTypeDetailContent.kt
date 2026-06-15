package com.ctb.presentation.pokemontypedetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ctb.design.compose.component.TypeChip
import com.ctb.design.compose.theme.PokemonTheme
import com.ctb.design.compose.theme.PokemonTypeColor
import com.ctb.design.compose.theme.Spacing
import com.ctb.domain.models.PokemonType
import com.ctb.domain.models.TypeEffectivenessChart
import com.ctb.presentation.R
import com.ctb.presentation.pokemontypedetail.viewmodel.PokemonTypeDetailState

@Composable
fun PokemonTypeDetailContent(
    state: PokemonTypeDetailState,
    onBackClick: () -> Unit,
) {
    Box(
        modifier =
        Modifier
            .fillMaxSize()
            .background(PokemonTypeColor.headerGradient(state.type)),
    ) {
        Text(
            text = stringResource(id = R.string.label_back_arrow),
            color = PokemonTypeColor.onTypeColor,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier =
            Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(Spacing.small)
                .clickable { onBackClick() },
        )

        Column(
            modifier =
            Modifier
                .fillMaxSize()
                .statusBarsPadding()
                // Keep content above the system nav bar instead of drawing behind it; the
                // gradient background remains visible in the inset gap.
                .navigationBarsPadding()
                .padding(horizontal = Spacing.medium)
                .padding(top = 64.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Text(
                text = state.type.apiName.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = PokemonTypeColor.onTypeColor,
            )

            Spacer(modifier = Modifier.height(Spacing.medium))

            state.effectiveness?.let { effectiveness ->
                TypeMatchupSection(
                    titleRes = R.string.label_weaknesses,
                    types = effectiveness.weaknesses,
                )
                TypeMatchupSection(
                    titleRes = R.string.label_resistances,
                    types = effectiveness.resistances,
                )
                TypeMatchupSection(
                    titleRes = R.string.label_immunities,
                    types = effectiveness.immunities,
                )
                TypeMatchupSection(
                    titleRes = R.string.label_super_effective_against,
                    types = effectiveness.superEffectiveAgainst,
                )
                TypeMatchupSection(
                    titleRes = R.string.label_not_very_effective_against,
                    types = effectiveness.notVeryEffectiveAgainst,
                )
                TypeMatchupSection(
                    titleRes = R.string.label_no_effect_against,
                    types = effectiveness.noEffectAgainst,
                )
            }
        }
    }
}

@Composable
private fun TypeMatchupSection(
    titleRes: Int,
    types: List<PokemonType>,
) {
    if (types.isEmpty()) return

    Column(
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(bottom = Spacing.small)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(Spacing.small),
    ) {
        Text(
            text = stringResource(id = titleRes),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(bottom = Spacing.xSmall),
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(Spacing.small),
            verticalArrangement = Arrangement.spacedBy(Spacing.small),
        ) {
            types.forEach { type -> TypeChip(type = type) }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PokemonTypeDetailContentPreview() {
    PokemonTheme {
        PokemonTypeDetailContent(
            state =
            PokemonTypeDetailState(
                type = PokemonType.BUG,
                effectiveness = TypeEffectivenessChart.effectivenessFor(PokemonType.BUG),
            ),
            onBackClick = {},
        )
    }
}
