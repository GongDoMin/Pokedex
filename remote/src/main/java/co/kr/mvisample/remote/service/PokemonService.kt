package co.kr.mvisample.remote.service

import co.kr.mvisample.remote.model.PokemonDetailResponse
import co.kr.mvisample.remote.model.PokemonResponseWithPaging
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonService {
    @GET("pokemon")
    suspend fun fetchPokemons(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): PokemonResponseWithPaging

    @GET("pokemon/{name}")
    suspend fun fetchPokemonDetail(
        @Path("name") name: String
    ): PokemonDetailResponse
}