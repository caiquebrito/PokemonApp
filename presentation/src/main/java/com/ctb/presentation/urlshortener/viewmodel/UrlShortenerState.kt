package com.ctb.presentation.urlshortener.viewmodel

import com.ctb.common.viewmodel.flow.UIState
import com.ctb.domain.models.ShortenUrlAlias

data class UrlShortenerState(
    val isLoading: Boolean = false,
    val shortenedAlias: List<ShortenUrlAlias> = emptyList(),
    val isInputInvalid: Boolean = false,
    val invalidInputMessage: String = "",
) : UIState
