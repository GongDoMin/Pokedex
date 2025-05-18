package co.kr.mvisample.remote.datasource

import co.kr.mvisample.remote.fake.FakePokemonService
import co.kr.mvisample.remote.impl.PokemonDataSourceImpl
import co.kr.mvisample.remote.model.PokemonDetailResponse
import co.kr.mvisample.remote.model.PokemonResponse
import co.kr.mvisample.remote.model.PokemonResponseWithPaging
import co.kr.mvisample.remote.model.TypeResponse
import co.kr.mvisample.remote.model.TypeWithSlotResponse
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class PokemonDataSourceTest : StringSpec() {

    private val pokemonService = FakePokemonService()
    private val pokemonDataSource = PokemonDataSourceImpl(pokemonService)

    init {
        "포켓몬 리스트 요청 테스트" {
            val exceptedPokemons = PokemonResponseWithPaging(
                count = 20,
                next = "",
                previous = "",
                results = listOf(
                    PokemonResponse("bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/"),
                    PokemonResponse("ivysaur", "https://pokeapi.co/api/v2/pokemon/2/"),
                    PokemonResponse("venusaur", "https://pokeapi.co/api/v2/pokemon/3/"),
                    PokemonResponse("charmander", "https://pokeapi.co/api/v2/pokemon/4/"),
                    PokemonResponse("charmeleon", "https://pokeapi.co/api/v2/pokemon/5/"),
                    PokemonResponse("charizard", "https://pokeapi.co/api/v2/pokemon/6/"),
                    PokemonResponse("squirtle", "https://pokeapi.co/api/v2/pokemon/7/"),
                    PokemonResponse("wartortle", "https://pokeapi.co/api/v2/pokemon/8/"),
                    PokemonResponse("blastoise", "https://pokeapi.co/api/v2/pokemon/9/"),
                    PokemonResponse("caterpie", "https://pokeapi.co/api/v2/pokemon/10/"),
                    PokemonResponse("metapod", "https://pokeapi.co/api/v2/pokemon/11/"),
                    PokemonResponse("butterfree", "https://pokeapi.co/api/v2/pokemon/12/"),
                    PokemonResponse("weedle", "https://pokeapi.co/api/v2/pokemon/13/"),
                    PokemonResponse("kakuna", "https://pokeapi.co/api/v2/pokemon/14/"),
                    PokemonResponse("beedrill", "https://pokeapi.co/api/v2/pokemon/15/"),
                    PokemonResponse("pidgey", "https://pokeapi.co/api/v2/pokemon/16/"),
                    PokemonResponse("pidgeotto", "https://pokeapi.co/api/v2/pokemon/17/"),
                    PokemonResponse("pidgeot", "https://pokeapi.co/api/v2/pokemon/18/"),
                    PokemonResponse("rattata", "https://pokeapi.co/api/v2/pokemon/19/"),
                    PokemonResponse("raticate", "https://pokeapi.co/api/v2/pokemon/20/")
                )
            )
            val pokemons = pokemonDataSource.fetchPokemons(20, 0)
            pokemons shouldBe exceptedPokemons
        }

        "포켓몬 상세 요청 테스트" {
            val exceptedPokemonDetail = PokemonDetailResponse(
                id = 6,
                name = "charizard",
                height = 17,
                weight = 905,
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
            val pokemonDetail = pokemonDataSource.fetchPokemonDetail("charizard")
            pokemonDetail shouldBe exceptedPokemonDetail
        }
    }
}