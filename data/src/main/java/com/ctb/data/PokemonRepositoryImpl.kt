package com.ctb.data

import com.ctb.domain.repositories.PokemonRepository

class PokemonRepositoryImpl(
    private val remoteDataSource: PokemonRemoteDataSource,
) : PokemonRepository {
    override fun getPokemonPage(
        limit: Int,
        offset: Int,
    ) = remoteDataSource.getPokemonPage(limit = limit, offset = offset)

    override fun getPokemonDetail(id: Int) = remoteDataSource.getPokemonDetail(id)

    override fun searchPokemonByName(name: String) = remoteDataSource.searchPokemonByName(name)
}
