package com.ctb.presentation.urlshortenerdetail

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ctb.common.ui.collectAsEffect
import com.ctb.domain.models.ShortenUrlAlias
import com.ctb.domain.models.ShortenUrlLink
import com.ctb.presentation.urlshortenerdetail.viewmodel.UrlShortenerDetailEffect
import com.ctb.presentation.urlshortenerdetail.viewmodel.UrlShortenerDetailViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun UrlShortenerDetailScreen(
    alias: String,
    linkSelf: String,
    linkShort: String,
    viewModel: UrlShortenerDetailViewModel = koinViewModel(),
    onBackClick: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    viewModel.effect.collectAsEffect { effect ->
        when (effect) {
            UrlShortenerDetailEffect.CloseScreen -> onBackClick.invoke()
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.onShortenerSelected(
            ShortenUrlAlias(
                alias = alias,
                links = ShortenUrlLink(self = linkSelf, short = linkShort),
            ),
        )
    }

    BackHandler {
        viewModel.onBack()
    }

    UrlShortenerDetailContent(
        state = state,
        onBackClick = viewModel::onBack,
    )
}
