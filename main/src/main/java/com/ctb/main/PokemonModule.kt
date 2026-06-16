package com.ctb.main

import com.ctb.common.rest.ApiServiceFactory
import com.ctb.common.rest.OkHttpClientFactory
import com.ctb.data.PokemonRemoteDataSource
import com.ctb.data.PokemonRepositoryImpl
import com.ctb.data_remote.api.PokemonAPI
import com.ctb.data_remote.repository.PokemonRemoteDataSourceImpl
import com.ctb.domain.repositories.PokemonRepository
import com.ctb.domain.usecase.GetEvolutionChainUseCase
import com.ctb.domain.usecase.GetPokemonDetailUseCase
import com.ctb.domain.usecase.GetPokemonPageUseCase
import com.ctb.domain.usecase.SearchPokemonUseCase
import com.ctb.presentation.pokemondetail.viewmodel.PokemonDetailViewModel
import com.ctb.presentation.pokemonhome.viewmodel.PokemonHomeViewModel
import com.ctb.presentation.pokemontypedetail.viewmodel.PokemonTypeDetailViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.context.loadKoinModules
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

object PokemonModule {
    internal var baseURL: String = ""
    internal var isDebug: Boolean = false

    fun injectFeature(
        baseURL: String,
        isDebug: Boolean,
    ) {
        PokemonModule.baseURL = baseURL
        PokemonModule.isDebug = isDebug
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
                    clazz = PokemonAPI::class.java,
                    endpoint = baseURL,
                    client = get(),
                )
            }
        }

    private val dataModule =
        module {
            factory<PokemonRemoteDataSource> {
                PokemonRemoteDataSourceImpl(
                    api = get(),
                )
            }
        }

    private val domainModule =
        module {
            single<PokemonRepository> {
                PokemonRepositoryImpl(
                    remoteDataSource = get(),
                )
            }
            factory {
                GetPokemonPageUseCase(
                    repository = get(),
                    dispatcher = Dispatchers.IO,
                )
            }
            factory {
                GetPokemonDetailUseCase(
                    repository = get(),
                    dispatcher = Dispatchers.IO,
                )
            }
            factory {
                SearchPokemonUseCase(
                    repository = get(),
                    dispatcher = Dispatchers.IO,
                )
            }
            factory {
                GetEvolutionChainUseCase(
                    repository = get(),
                    dispatcher = Dispatchers.IO,
                )
            }
        }

    private val presentationModule =
        module {
            viewModel {
                PokemonHomeViewModel(
                    getPokemonPageUseCase = get(),
                    searchPokemonUseCase = get(),
                    resourceProvider = get(),
                )
            }
            viewModel {
                PokemonDetailViewModel(
                    getPokemonDetailUseCase = get(),
                    getEvolutionChainUseCase = get(),
                    resourceProvider = get(),
                )
            }
            viewModel {
                PokemonTypeDetailViewModel()
            }
        }
}
