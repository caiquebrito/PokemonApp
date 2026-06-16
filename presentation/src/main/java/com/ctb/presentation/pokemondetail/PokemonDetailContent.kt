package com.ctb.presentation.pokemondetail

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ctb.design.compose.component.PokemonImage
import com.ctb.design.compose.component.TypeChip
import com.ctb.design.compose.theme.PokemonTheme
import com.ctb.design.compose.theme.PokemonTypeColor
import com.ctb.design.compose.theme.Spacing
import com.ctb.domain.models.EvolutionChain
import com.ctb.domain.models.PokemonDetail
import com.ctb.domain.models.PokemonMove
import com.ctb.domain.models.PokemonStat
import com.ctb.domain.models.PokemonType
import com.ctb.presentation.R
import com.ctb.presentation.pokemondetail.viewmodel.PokemonDetailState
import kotlin.math.roundToInt
import com.ctb.design.R as DesignR

private const val DECIMETRES_PER_METRE = 10.0
private const val HECTOGRAMS_PER_KILOGRAM = 10.0
private const val ID_PAD_LENGTH = 3
private const val STAT_ANIMATION_MILLIS = 800
private const val CARD_HEIGHT_FRACTION = 0.74f
private const val MAX_MAIN_MOVES = 12
private const val MOVE_CHIP_ALPHA = 0.18f

@Composable
fun PokemonDetailContent(
    state: PokemonDetailState,
    onBackClick: () -> Unit,
    onTypeClick: (PokemonType) -> Unit,
) {
    val detail = state.detail
    val primaryType = detail?.primaryType ?: PokemonType.UNKNOWN

    Box(
        modifier =
        Modifier
            .fillMaxSize()
            .background(PokemonTypeColor.headerGradient(primaryType)),
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

        when {
            state.isLoading && detail == null ->
                CircularProgressIndicator(
                    color = PokemonTypeColor.onTypeColor,
                    modifier = Modifier.align(Alignment.Center),
                )

            detail != null -> {
                DetailCard(
                    detail = detail,
                    evolutionChain = state.evolutionChain,
                    onTypeClick = onTypeClick,
                )
                PokemonImage(
                    imageUrl = detail.imageUrl,
                    contentDescription = detail.name,
                    modifier =
                    Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 72.dp)
                        .size(200.dp),
                )
            }
        }
    }
}

@Composable
private fun BoxScope.DetailCard(
    detail: PokemonDetail,
    evolutionChain: EvolutionChain?,
    onTypeClick: (PokemonType) -> Unit,
) {
    val maxStat = detail.stats.maxOfOrNull { it.baseValue } ?: 1

    Column(
        modifier =
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(CARD_HEIGHT_FRACTION)
            .align(Alignment.BottomCenter)
            // Keep the card's content above the system nav bar instead of drawing behind it; the
            // gradient background remains visible in the inset gap.
            .navigationBarsPadding()
            .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(rememberScrollState())
            .padding(
                start = Spacing.medium,
                end = Spacing.medium,
                top = Spacing.medium,
                bottom = Spacing.medium,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(64.dp))

        Text(
            text = "#${detail.id.toString().padStart(ID_PAD_LENGTH, '0')} ${detail.name}",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(Spacing.small))

        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.small)) {
            detail.types.forEach { type -> TypeChip(type = type, onClick = { onTypeClick(type) }) }
        }

        Spacer(modifier = Modifier.height(Spacing.medium))

        MeasurementsRow(detail = detail)

        Spacer(modifier = Modifier.height(Spacing.medium))

        Text(
            text = stringResource(id = R.string.label_base_stats),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(bottom = Spacing.xSmall),
        )

        detail.stats.forEach { stat ->
            StatBar(stat = stat, maxValue = maxStat)
            Spacer(modifier = Modifier.height(Spacing.xSmall))
        }

        if (evolutionChain != null && evolutionChain.steps.size > 1) {
            Spacer(modifier = Modifier.height(Spacing.medium))
            EvolutionChainSection(chain = evolutionChain)
        }

        if (detail.moves.isNotEmpty()) {
            Spacer(modifier = Modifier.height(Spacing.medium))
            MovesSection(
                moves = detail.moves.take(MAX_MAIN_MOVES),
                accent = PokemonTypeColor.colorFor(detail.primaryType),
            )
        }
    }
}

@Composable
private fun MovesSection(
    moves: List<PokemonMove>,
    accent: Color,
) {
    Text(
        text = stringResource(id = R.string.label_moves),
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(bottom = Spacing.xSmall),
    )
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.xSmall),
        verticalArrangement = Arrangement.spacedBy(Spacing.xSmall),
    ) {
        moves.forEach { move -> MoveChip(move = move, accent = accent) }
    }
}

@Composable
private fun MoveChip(
    move: PokemonMove,
    accent: Color,
) {
    Box(
        modifier =
        Modifier
            .clip(CircleShape)
            .background(accent.copy(alpha = MOVE_CHIP_ALPHA))
            .padding(horizontal = Spacing.small, vertical = 6.dp),
    ) {
        Text(
            text = move.name.split('-').joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } },
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

@Composable
private fun MeasurementsRow(detail: PokemonDetail) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.large),
    ) {
        Measurement(
            iconRes = DesignR.drawable.ic_weight,
            description = stringResource(id = R.string.description_weight),
            value = "${detail.weightHectograms / HECTOGRAMS_PER_KILOGRAM}kg",
        )
        Box(
            modifier =
            Modifier
                .width(1.dp)
                .height(48.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant),
        )
        Measurement(
            iconRes = DesignR.drawable.ic_height,
            description = stringResource(id = R.string.description_height),
            value = "${detail.heightDecimetres / DECIMETRES_PER_METRE}m",
        )
    }
}

@Composable
private fun Measurement(
    iconRes: Int,
    description: String,
    value: String,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = description,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
            modifier = Modifier.size(Spacing.medium),
        )
        Spacer(modifier = Modifier.height(Spacing.xSmall))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun StatBar(
    stat: PokemonStat,
    maxValue: Int,
) {
    val target = if (maxValue > 0) stat.baseValue.toFloat() / maxValue else 0f
    var started by remember { mutableStateOf(false) }
    val fraction by animateFloatAsState(
        targetValue = if (started) target else 0f,
        animationSpec = tween(durationMillis = STAT_ANIMATION_MILLIS),
        label = "statBarFraction",
    )
    LaunchedEffect(stat.baseValue, maxValue) { started = true }

    // The displayed number is derived from the same animated fraction, so it counts up from 0
    // to the real value in lock-step with the bar.
    val animatedValue = (fraction * maxValue).roundToInt()

    Box(
        modifier =
        Modifier
            .fillMaxWidth()
            .height(26.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Box(
            modifier =
            Modifier
                .fillMaxHeight()
                .fillMaxWidth(fraction)
                .clip(CircleShape)
                .background(PokemonTypeColor.statColor(stat.name)),
        ) {
            Text(
                text = animatedValue.toString(),
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelLarge,
                maxLines = 1,
                modifier =
                Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = Spacing.xSmall),
            )
        }
        Text(
            text = statLabel(stat.name),
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelLarge,
            maxLines = 1,
            modifier =
            Modifier
                .align(Alignment.CenterStart)
                .padding(start = Spacing.small),
        )
    }
}

private fun statLabel(apiName: String): String =
    when (apiName.lowercase()) {
        "hp" -> "HP"
        "attack" -> "Atk"
        "defense" -> "Def"
        "special-attack" -> "SpAtk"
        "special-defense" -> "SpDef"
        "speed" -> "Spd"
        else -> apiName.replaceFirstChar { it.uppercase() }
    }

@Preview(showBackground = true)
@Composable
private fun PokemonDetailContentPreview() {
    PokemonTheme {
        PokemonDetailContent(
            state =
            PokemonDetailState(
                isLoading = false,
                detail =
                PokemonDetail(
                    id = 257,
                    name = "Blaziken",
                    imageUrl = "",
                    types = listOf(PokemonType.FIRE, PokemonType.FIGHTING),
                    heightDecimetres = 19,
                    weightHectograms = 520,
                    stats =
                    listOf(
                        PokemonStat("hp", 80),
                        PokemonStat("attack", 120),
                        PokemonStat("defense", 70),
                        PokemonStat("special-attack", 110),
                        PokemonStat("special-defense", 70),
                        PokemonStat("speed", 80),
                    ),
                    moves =
                    listOf(
                        PokemonMove("scratch", 1),
                        PokemonMove("ember", 7),
                        PokemonMove("double-kick", 16),
                        PokemonMove("blaze-kick", 36),
                    ),
                ),
            ),
            onBackClick = {},
            onTypeClick = {},
        )
    }
}
