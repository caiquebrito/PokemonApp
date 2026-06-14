package com.ctb.presentation.urlshortener.viewmodel

import com.ctb.common.ui.ResourceProvider
import com.ctb.commonkotlin.test.collectTestFlows
import com.ctb.commonkotlin.test.mockFailure
import com.ctb.commonkotlin.test.mockSuccess
import com.ctb.commonkotlin.usecases.CoroutinesTestRule
import com.ctb.domain.models.ShortenUrlAlias
import com.ctb.domain.models.ShortenUrlLink
import com.ctb.domain.usecase.ShortenUrlUseCase
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class UrlShortenerViewModelTest {
    @get:Rule
    val mainDispatcherRule = CoroutinesTestRule()

    private val shortenUrlUseCase = mockk<ShortenUrlUseCase>()
    private val resourceProvider = mockk<ResourceProvider>(relaxed = true)

    private val viewModel = UrlShortenerViewModel(shortenUrlUseCase, resourceProvider)

    @Test
    fun `shortenUrl success updates state and stops loading`() =
        runTest {
            val mockUrl = "https://short"
            shortenUrlUseCase.mockSuccess(getMockShortenUrl())
            collectTestFlows(viewModel.state, viewModel.effect) { state, effect ->
                viewModel.shortenUrl(url = mockUrl)
                state.assertValuesInOrder(
                    UrlShortenerState(isLoading = false),
                    UrlShortenerState(
                        isLoading = false,
                        shortenedAlias = listOf(getMockShortenUrl()),
                    ),
                )
            }
        }

    @Test
    fun `shortenUrl failure emits showError effect and stops loading`() =
        runTest {
            val mockUrl = "https://short"
            shortenUrlUseCase.mockFailure()
            collectTestFlows(viewModel.state, viewModel.effect) { state, effect ->
                viewModel.shortenUrl(url = mockUrl)
                state.assertValuesInOrder(
                    UrlShortenerState(isLoading = false),
                )
                effect.assertValuesInOrder(
                    UrlShortenerEffect.ShowError(message = ""),
                )
            }
        }

    fun getMockShortenUrl() =
        ShortenUrlAlias(
            alias = "abcd",
            links =
            ShortenUrlLink(
                self = "https://self.com/abcd",
                short = "https://short.com/abcd",
            ),
        )
}
