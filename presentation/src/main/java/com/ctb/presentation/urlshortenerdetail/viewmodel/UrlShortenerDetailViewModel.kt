package com.ctb.presentation.urlshortenerdetail.viewmodel

import com.ctb.common.viewmodel.flow.ViewModel
import com.ctb.domain.models.ShortenUrlAlias

class UrlShortenerDetailViewModel : ViewModel<UrlShortenerDetailState, UrlShortenerDetailEffect>(
    initialState = UrlShortenerDetailState(),
) {

    fun onShortenerSelected(item: ShortenUrlAlias) {
        setState { it.copy(alias = item, isLoading = false) }
    }

    fun onCreate() = Unit

    fun onBack() {
        sendEffect(UrlShortenerDetailEffect.CloseScreen)
    }
}
