package com.ctb.presentation.urlshortener

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ctb.design.compose.theme.BorderRadius
import com.ctb.design.compose.theme.Spacing
import com.ctb.domain.models.ShortenUrlAlias
import com.ctb.domain.models.ShortenUrlLink
import com.ctb.presentation.R
import com.ctb.design.R as DesignR

@Composable
fun UrlShortenerContent(
    isLoading: Boolean,
    items: List<ShortenUrlAlias>,
    isInputInvalid: Boolean,
    invalidInputMessage: String,
    onTextChanged: (String) -> Unit,
    onSend: (String) -> Unit = {},
    onItemClicked: (ShortenUrlAlias) -> Unit = {},
) {
    var text by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { padding: PaddingValues ->
        Column(
            modifier =
            Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            Column(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFF6750A4),
                        shape =
                        RoundedCornerShape(
                            bottomStart = Spacing.medium,
                            bottomEnd = Spacing.medium,
                        ),
                    )
                    .padding(horizontal = Spacing.small, vertical = Spacing.small),
            ) {
                Box(
                    modifier =
                    Modifier
                        .background(
                            Color.Transparent,
                            shape = BorderRadius.SMALL,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        painter = painterResource(id = DesignR.drawable.app_logo),
                        contentDescription = stringResource(id = R.string.description_app_logo),
                        modifier = Modifier.size(72.dp),
                    )
                }
                Row(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = Spacing.xSmall),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xSmall),
                ) {
                    OutlinedTextField(
                        isError = isInputInvalid,
                        value = text,
                        onValueChange = { new ->
                            onTextChanged.invoke(new)
                            text = new
                        },
                        modifier =
                        Modifier
                            .weight(1f)
                            .semantics { testTagsAsResourceId = true }
                            .testTag("input_text"),
                        shape = BorderRadius.LARGE,
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.input_placeholder),
                                color = Color.White.copy(alpha = 0.6f),
                            )
                        },
                        singleLine = true,
                        colors =
                        OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.7f),
                            cursorColor = Color.White,
                            errorBorderColor = Color.White,
                            errorSupportingTextColor = Color.White,
                            errorCursorColor = Color.White,
                        ),
                    )

                    Button(
                        enabled = text.isNotBlank() && !isLoading && !isInputInvalid,
                        colors =
                        ButtonColors(
                            containerColor = Color(0xFF530372),
                            contentColor = Color.White,
                            disabledContentColor = Color.Gray,
                            disabledContainerColor = Color.Gray,
                        ),
                        onClick = {
                            if (text.isNotBlank() && !isInputInvalid) {
                                onSend(text)
                                text = ""
                            }
                        },
                    ) {
                        Image(
                            painter = painterResource(id = DesignR.drawable.chevron_right),
                            contentDescription = stringResource(id = R.string.description_send_url),
                            modifier = Modifier.size(Spacing.medium),
                            colorFilter = ColorFilter.tint(color = Color.White),
                        )
                    }
                }
                if (isInputInvalid) {
                    Text(
                        text = invalidInputMessage,
                        color = Color.Red,
                    )
                }
            }

            Column(
                modifier =
                Modifier
                    .fillMaxSize()
                    .padding(top = Spacing.xSmall),
            ) {
                Text(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(start = Spacing.small, top = Spacing.small),
                    style = MaterialTheme.typography.titleMedium,
                    text = stringResource(id = R.string.text_recent_shortened_url),
                )
                LazyColumn(
                    modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(vertical = Spacing.xSmall),
                ) {
                    items(items = items) { item: ShortenUrlAlias ->
                        ShortenUrlListItem(item, onItemClicked)
                    }
                    if (isLoading) {
                        item {
                            ShimmerListItem()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ShortenUrlListItem(
    item: ShortenUrlAlias,
    onItemClicked: (ShortenUrlAlias) -> Unit,
) {
    Column(
        modifier =
        Modifier
            .fillMaxWidth()
            .clickable { onItemClicked(item) }
            .padding(horizontal = Spacing.small, vertical = Spacing.xSmall),
    ) {
        Text(text = item.links.self, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = item.links.short, style = MaterialTheme.typography.bodyMedium)
    }
    HorizontalDivider()
}

@Composable
private fun ShimmerListItem() {
    val shimmerColors =
        listOf(
            Color.LightGray.copy(alpha = 0.6f),
            Color.LightGray.copy(alpha = 0.3f),
            Color.LightGray.copy(alpha = 0.6f),
        )

    val transition = rememberInfiniteTransition()
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec =
        infiniteRepeatable(
            animation = tween(durationMillis = 900),
            repeatMode = RepeatMode.Restart,
        ),
    )

    val brush =
        Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(translateAnim, 0f),
            end = Offset(translateAnim + 200f, 0f),
        )

    Column(
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.small, vertical = Spacing.small),
    ) {
        Box(
            modifier =
            Modifier
                .fillMaxWidth()
                .height(Spacing.xSmall)
                .background(brush, shape = BorderRadius.XSMALL),
        )
        Spacer(modifier = Modifier.height(Spacing.xSmall))
        Box(
            modifier =
            Modifier
                .fillMaxWidth(0.6f)
                .height(14.dp)
                .background(brush, shape = BorderRadius.XSMALL),
        )
    }
    HorizontalDivider()
}

@Preview(showBackground = true)
@Composable
fun UrlShortenerContentPreview() {
    val sample =
        List(5) { index ->
            ShortenUrlAlias(
                alias = "alias-$index",
                links =
                ShortenUrlLink(self = index.toString(), short = "$index short"),
            )
        }

    UrlShortenerContent(
        isInputInvalid = false,
        invalidInputMessage = "",
        items = sample,
        isLoading = true,
        onTextChanged = {},
        onSend = {},
    )
}
