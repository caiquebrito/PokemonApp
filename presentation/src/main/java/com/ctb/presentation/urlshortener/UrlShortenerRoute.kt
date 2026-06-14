package com.ctb.presentation.urlshortener

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object UrlShortenerRoute

fun NavController.openUrlShortener() {
    navigate(UrlShortenerRoute)
}

fun NavGraphBuilder.urlShortener(
    closeFlow: () -> Unit,
    onItemClicked: (com.ctb.domain.models.ShortenUrlAlias) -> Unit,
) =
    composable<UrlShortenerRoute> {
        UrlShortenerScreen(
            closeFlow = closeFlow,
            onItemClicked = onItemClicked,
        )
    }
