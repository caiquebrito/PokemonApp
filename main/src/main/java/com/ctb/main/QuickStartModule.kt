package com.ctb.main

import com.ctb.common.rest.ApiServiceFactory
import com.ctb.common.rest.OkHttpClientFactory
import com.ctb.data.ShortenUrlRemoteDataSource
import com.ctb.data.ShortenUrlRepositoryImpl
import com.ctb.data_remote.api.ShortenUrlAPI
import com.ctb.data_remote.repository.ShortenUrlRemoteDataSourceImpl
import com.ctb.domain.repositories.ShortenUrlRepository
import com.ctb.domain.usecase.ShortenUrlUseCase
import com.ctb.presentation.urlshortener.viewmodel.UrlShortenerViewModel
import com.ctb.presentation.urlshortenerdetail.viewmodel.UrlShortenerDetailViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.context.loadKoinModules
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

object QuickStartModule {
    internal var baseURL: String = ""
    internal var isDebug: Boolean = false

    fun injectFeature(
        baseURL: String,
        isDebug: Boolean,
    ) {
        QuickStartModule.baseURL = baseURL
        QuickStartModule.isDebug = isDebug
        loadKoinModules(loadFeature)
    }

    internal val loadFeature by lazy {
        listOf(
            kotlinModule,
            dataRemoteModule,
            dataModule,
            domainModule,
            presentationModule,
        )
    }

    private val kotlinModule =
        module {
            single<CoroutineDispatcher> { Dispatchers.IO }
        }

    private val dataRemoteModule =
        module {
            single {
                OkHttpClientFactory.makeLoggingInterceptor(isDebug)
            }
            single {
                OkHttpClientFactory.makeOkHttpClient(
                    httpLoggingInterceptor = get(),
                )
            }
            single {
                ApiServiceFactory.create(
                    clazz = ShortenUrlAPI::class.java,
                    endpoint = baseURL,
                    client = get(),
                )
            }
        }

    private val dataModule =
        module {
            factory<ShortenUrlRemoteDataSource> {
                ShortenUrlRemoteDataSourceImpl(
                    api = get(),
                )
            }
        }

    private val domainModule =
        module {
            single<ShortenUrlRepository> {
                ShortenUrlRepositoryImpl(
                    remoteDataSource = get(),
                )
            }
            factory {
                ShortenUrlUseCase(
                    repository = get(),
                    dispatcher = Dispatchers.IO,
                )
            }
        }

    private val presentationModule =
        module {
            viewModel {
                UrlShortenerViewModel(
                    shortenUrlUseCase = get(),
                    resourceProvider = get(),
                )
            }
            viewModel {
                UrlShortenerDetailViewModel()
            }
        }
}
