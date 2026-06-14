package com.ctb.presentation.urlshortener.viewmodel

import com.ctb.common.viewmodel.flow.UIEffect

sealed class UrlShortenerEffect : UIEffect {
    object CloseFlow : UrlShortenerEffect()

    data class ShowError(
        val message: String,
    ) : UrlShortenerEffect()
}
