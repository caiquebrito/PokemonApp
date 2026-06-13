package com.ctb.presentation.urlshortenerdetail.viewmodel

import com.ctb.common.viewmodel.flow.UIState
import com.ctb.domain.models.ShortenUrlAlias

data class UrlShortenerDetailState(
    val alias: ShortenUrlAlias? = null,
    val isLoading: Boolean = true,
) : UIState
