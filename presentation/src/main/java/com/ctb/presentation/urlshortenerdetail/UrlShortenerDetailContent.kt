package com.ctb.presentation.urlshortenerdetail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.ctb.design.compose.theme.BorderRadius
import com.ctb.design.compose.theme.Spacing
import com.ctb.domain.models.ShortenUrlAlias
import com.ctb.domain.models.ShortenUrlLink
import com.ctb.presentation.urlshortenerdetail.viewmodel.UrlShortenerDetailState

@Composable
fun UrlShortenerDetailContent(
    state: UrlShortenerDetailState,
    onBackClick: () -> Unit,
) {
    BackHandler(onBack = onBackClick)

    val backgroundPurple = Color(0xFF6750A4)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { padding ->
        Column(
            modifier =
            Modifier
                .fillMaxSize()
                .padding(padding)
                .background(backgroundPurple)
                .padding(Spacing.small),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            state.alias?.let { item ->
                Column(
                    modifier =
                    Modifier
                        .fillMaxWidth(0.9f)
                        .background(
                            color = Color.White.copy(alpha = 0.12f),
                            shape = BorderRadius.MEDIUM,
                        )
                        .padding(Spacing.medium),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = item.alias,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )

                    Text(
                        modifier = Modifier.padding(top = Spacing.xSmall),
                        text = item.links.self,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White.copy(alpha = 0.85f),
                    )

                    Text(
                        modifier = Modifier.padding(top = Spacing.small),
                        text = item.links.short,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.75f),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UrlShortenerDetailContentPreview() {
    val sampleAlias =
        ShortenUrlAlias(
            alias = "my-short-link",
            links = ShortenUrlLink(
                self = "https://example.com/long/url",
                short = "https://short.link/abc",
            ),
        )

    UrlShortenerDetailContent(
        state = UrlShortenerDetailState(alias = sampleAlias, isLoading = false),
        onBackClick = {},
    )
}
