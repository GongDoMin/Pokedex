package co.kr.mvisample.remote.fake

import co.kr.mvisample.remote.model.PokemonDetailResponse
import co.kr.mvisample.remote.model.PokemonResponseWithPaging
import co.kr.mvisample.remote.service.PokemonService
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.File

@OptIn(ExperimentalSerializationApi::class)
class FakePokemonService(
    val json: Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }
) : PokemonService {

    private val pokemons = File("src/main/assets/pokemon.json")
    private val pokemonDetail = File("src/main/assets/pokemon_detail.json")

    override suspend fun fetchPokemons(limit: Int, offset: Int): PokemonResponseWithPaging =
        json.decodeFromStream(pokemons.inputStream())

    override suspend fun fetchPokemonDetail(name: String): PokemonDetailResponse =
        json.decodeFromStream(pokemonDetail.inputStream())
}