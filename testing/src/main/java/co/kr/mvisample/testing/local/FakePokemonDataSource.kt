package co.kr.mvisample.testing.local

import co.kr.mvisample.remote.datasource.PokemonDataSource
import co.kr.mvisample.remote.model.PokemonDetailResponse
import co.kr.mvisample.remote.model.PokemonResponse
import co.kr.mvisample.remote.model.PokemonResponseWithPaging
import co.kr.mvisample.remote.model.TypeResponse
import co.kr.mvisample.remote.model.TypeWithSlotResponse

class FakePokemonDataSource : PokemonDataSource {

    override suspend fun fetchPokemons(limit: Int, offset: Int): PokemonResponseWithPaging =
        PokemonResponseWithPaging(
            count = 20,
            next = "",
            previous = "",
            results = listOf(
                PokemonResponse(name = "bulbasaur", url = "https://pokeapi.co/api/v2/pokemon/1/"),
                PokemonResponse(name = "ivysaur", url = "https://pokeapi.co/api/v2/pokemon/2/"),
                PokemonResponse(name = "venusaur", url = "https://pokeapi.co/api/v2/pokemon/3/"),
                PokemonResponse(name = "charmander", url = "https://pokeapi.co/api/v2/pokemon/4/"),
                PokemonResponse(name = "charmeleon", url = "https://pokeapi.co/api/v2/pokemon/5/"),
                PokemonResponse(name = "charizard", url = "https://pokeapi.co/api/v2/pokemon/6/"),
                PokemonResponse(name = "squirtle", url = "https://pokeapi.co/api/v2/pokemon/7/"),
                PokemonResponse(name = "wartortle", url = "https://pokeapi.co/api/v2/pokemon/8/"),
                PokemonResponse(name = "blastoise", url = "https://pokeapi.co/api/v2/pokemon/9/"),
                PokemonResponse(name = "caterpie", url = "https://pokeapi.co/api/v2/pokemon/10/"),
                PokemonResponse(name = "metapod", url = "https://pokeapi.co/api/v2/pokemon/11/"),
                PokemonResponse(name = "butterfree", url = "https://pokeapi.co/api/v2/pokemon/12/"),
                PokemonResponse(name = "weedle", url = "https://pokeapi.co/api/v2/pokemon/13/"),
                PokemonResponse(name = "kakuna", url = "https://pokeapi.co/api/v2/pokemon/14/"),
                PokemonResponse(name = "beedrill", url = "https://pokeapi.co/api/v2/pokemon/15/"),
                PokemonResponse(name = "pidgey", url = "https://pokeapi.co/api/v2/pokemon/16/"),
                PokemonResponse(name = "pidgeotto", url = "https://pokeapi.co/api/v2/pokemon/17/"),
                PokemonResponse(name = "pidgeot", url = "https://pokeapi.co/api/v2/pokemon/18/"),
                PokemonResponse(name = "rattata", url = "https://pokeapi.co/api/v2/pokemon/19/"),
                PokemonResponse(name = "raticate", url = "https://pokeapi.co/api/v2/pokemon/20/")
            )
        )

    override suspend fun fetchPokemonDetail(name: String): PokemonDetailResponse =
        PokemonDetailResponse(
            id = 6,
            name = "charizard",
            weight = 905,
            height = 17,
            types = listOf(
                TypeWithSlotResponse(
                    slot = 1,
                    type = TypeResponse(
                        name = "fire",
                        url = "https://pokeapi.co/api/v2/type/10/"
                    )
                ),
                TypeWithSlotResponse(
                    slot = 2,
                    type = TypeResponse(
                        name = "flying",
                        url = "https://pokeapi.co/api/v2/type/3/"
                    )
                )
            )
        )
}