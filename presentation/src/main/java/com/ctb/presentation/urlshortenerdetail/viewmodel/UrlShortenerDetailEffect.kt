package com.ctb.presentation.urlshortenerdetail.viewmodel

import com.ctb.common.viewmodel.flow.UIEffect

sealed interface UrlShortenerDetailEffect : UIEffect {
    object CloseScreen : UrlShortenerDetailEffect
}
