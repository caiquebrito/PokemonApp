package com.ctb.presentation.urlshortenerdetail

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.ctb.domain.models.ShortenUrlAlias
import kotlinx.serialization.Serializable

@Serializable
data class UrlShortenerDetailRoute(
    val alias: String,
    val linkSelf: String,
    val linkShort: String,
)

fun NavController.openUrlShortenerDetail(item: ShortenUrlAlias) {
    navigate(
        UrlShortenerDetailRoute(
            alias = item.alias,
            linkSelf = item.links.self,
            linkShort = item.links.short,
        ),
    )
}

fun NavGraphBuilder.urlShortenerDetail(onBackClick: () -> Unit) =
    composable<UrlShortenerDetailRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<UrlShortenerDetailRoute>()
        UrlShortenerDetailScreen(
            alias = route.alias,
            linkSelf = route.linkSelf,
            linkShort = route.linkShort,
            onBackClick = onBackClick,
        )
    }
