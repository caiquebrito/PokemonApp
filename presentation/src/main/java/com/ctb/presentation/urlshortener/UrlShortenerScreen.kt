package com.ctb.presentation.urlshortener

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ctb.common.ui.collectAsEffect
import com.ctb.presentation.urlshortener.viewmodel.UrlShortenerEffect
import com.ctb.presentation.urlshortener.viewmodel.UrlShortenerViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun UrlShortenerScreen(
    viewModel: UrlShortenerViewModel = koinViewModel(),
    closeFlow: () -> Unit,
    onItemClicked: (com.ctb.domain.models.ShortenUrlAlias) -> Unit,
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    viewModel.effect.collectAsEffect { effect ->
        when (effect) {
            UrlShortenerEffect.CloseFlow -> {
                closeFlow.invoke()
            }

            is UrlShortenerEffect.ShowError -> {
                Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    UrlShortenerContent(
        isLoading = state.isLoading,
        items = state.shortenedAlias,
        isInputInvalid = state.isInputInvalid,
        invalidInputMessage = state.invalidInputMessage,
        onTextChanged = viewModel::onInputTextChanged,
        onSend = viewModel::shortenUrl,
        onItemClicked = onItemClicked,
    )

    LaunchedEffect(key1 = Unit) {
        viewModel.onCreate()
    }

    BackHandler {
        closeFlow.invoke()
    }
}
