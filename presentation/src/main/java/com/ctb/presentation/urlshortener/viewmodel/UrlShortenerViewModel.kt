package com.ctb.presentation.urlshortener.viewmodel

import androidx.lifecycle.viewModelScope
import com.ctb.common.ui.ResourceProvider
import com.ctb.common.viewmodel.flow.ViewModel
import com.ctb.commonkotlin.usecases.onFailure
import com.ctb.commonkotlin.usecases.onSuccess
import com.ctb.domain.usecase.ShortenUrlUseCase
import com.ctb.presentation.R
import kotlinx.coroutines.launch

private const val REGEX_WEBSITE_PATTERN =
    "^(https?://)?(www\\.)?([a-zA-Z0-9]+[a-zA-Z0-9-]*\\.)+[a-zA-Z]{2,}(/.*)?$"

class UrlShortenerViewModel(
    private val shortenUrlUseCase: ShortenUrlUseCase,
    private val resourceProvider: ResourceProvider,
) : ViewModel<UrlShortenerState, UrlShortenerEffect>(UrlShortenerState()) {
    val regex = Regex(REGEX_WEBSITE_PATTERN)

    fun onCreate() {
        // initialize any metrics here, like analytics
    }

    fun shortenUrl(url: String) {
        viewModelScope.launch {
            setState { it.copy(isLoading = true) }

            shortenUrlUseCase(ShortenUrlUseCase.ParamShortenerUrl(url)).collect { result ->
                result.onSuccess { alias ->
                    setState { it.copy(shortenedAlias = it.shortenedAlias + alias) }
                }

                result.onFailure {
                    sendEffect(
                        UrlShortenerEffect.ShowError(
                            message =
                            it.message
                                ?: resourceProvider.getString(resId = R.string.error_shortener_message_null),
                        ),
                    )
                }
            }

            setState { it.copy(isLoading = false) }
        }
    }

    fun onInputTextChanged(newValue: String) {
        if (newValue.isNotEmpty() && !regex.matches(newValue)) {
            setState {
                it.copy(
                    isInputInvalid = true,
                    invalidInputMessage = resourceProvider.getString(resId = R.string.input_invalid_url),
                )
            }
        } else {
            setState {
                it.copy(
                    isInputInvalid = false,
                )
            }
        }
    }
}
