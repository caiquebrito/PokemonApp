package com.ctb.data_remote.repository

import com.ctb.data.PokemonRemoteDataSource
import com.ctb.data_remote.api.PokemonAPI
import com.ctb.data_remote.response.toDomain
import com.ctb.data_remote.response.toPokemon
import com.ctb.domain.models.Pokemon
import com.ctb.domain.models.PokemonDetail
import com.ctb.domain.models.PokemonPage
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class PokemonRemoteDataSourceImpl(
    private val api: PokemonAPI,
) : PokemonRemoteDataSource {

    override fun getPokemonPage(
        limit: Int,
        offset: Int,
    ): Flow<PokemonPage> =
        flow {
            val list = api.getPokemonList(limit = limit, offset = offset)

            // Each grid card needs the type + sprite, so fetch every item's detail concurrently.
            val items =
                coroutineScope {
                    list.results
                        .map { item -> async { api.getPokemonDetail(item.extractId()).toPokemon() } }
                        .awaitAll()
                }

            val hasMore = list.next != null
            emit(
                PokemonPage(
                    items = items,
                    nextOffset = if (hasMore) offset + limit else null,
                    hasMore = hasMore,
                ),
            )
        }.catch {
            throw it
        }

    override fun getPokemonDetail(id: Int): Flow<PokemonDetail> =
        flow {
            emit(api.getPokemonDetail(id).toDomain())
        }.catch {
            throw it
        }

    override fun searchPokemonByName(name: String): Flow<Pokemon> =
        flow {
            emit(api.getPokemonByName(name).toPokemon())
        }.catch {
            throw it
        }
}
