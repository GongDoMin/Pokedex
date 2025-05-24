package co.kr.mvisample.remote.impl

import co.kr.mvisample.remote.datasource.PokemonDataSource
import co.kr.mvisample.remote.model.PokemonDetailResponse
import co.kr.mvisample.remote.model.PokemonResponseWithPaging
import co.kr.mvisample.remote.service.PokemonService
import javax.inject.Inject

class PokemonDataSourceImpl @Inject constructor(
    private val pokemonService: PokemonService
) : PokemonDataSource {
    override suspend fun fetchPokemons(limit: Int, offset: Int): PokemonResponseWithPaging {
        val response = pokemonService.fetchPokemons(
            limit = limit,
            offset = offset
        )
        return response
    }

    override suspend fun fetchPokemonDetail(name: String): PokemonDetailResponse {
        val response = pokemonService.fetchPokemonDetail(name)

        return response
    }
}