package co.kr.mvisample.remote.datasource

import co.kr.mvisample.remote.model.PokemonDetailResponse
import co.kr.mvisample.remote.model.PokemonResponseWithPaging

interface PokemonDataSource {
    suspend fun fetchPokemons(limit: Int, offset: Int): PokemonResponseWithPaging

    suspend fun fetchPokemonDetail(name: String): PokemonDetailResponse
}