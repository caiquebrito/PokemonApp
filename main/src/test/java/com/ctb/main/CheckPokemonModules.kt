package com.ctb.main

import android.content.Context
import com.ctb.common.rest.OkHttpClientFactory
import com.ctb.common.ui.ResourceProvider
import com.ctb.main.PokemonModule.loadFeature
import io.mockk.mockk
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.verify.verify

class CheckPokemonModules : KoinTest {
    private val mockModule =
        module {
            single { HttpLoggingInterceptor() }
            single {
                OkHttpClientFactory.makeOkHttpClient(httpLoggingInterceptor = get())
            }
            // Provided at runtime by :common via Common.init(); mocked here so the
            // feature graph can be verified in isolation.
            single<ResourceProvider> { mockk() }
        }

    @Before
    fun setup() {
        PokemonModule.baseURL = "https://mockurl.pokemon.com"

        startKoin {
            androidContext(mockk<Context>())
            modules(mockModule, *loadFeature.toTypedArray())
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun `should verify koin graph injection`() {
        val modules = module { includes(mockModule, *loadFeature.toTypedArray()) }
        modules.verify()
    }
}
