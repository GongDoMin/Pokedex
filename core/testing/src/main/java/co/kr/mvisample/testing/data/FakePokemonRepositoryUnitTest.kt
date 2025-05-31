package co.kr.mvisample.testing.data

import androidx.paging.PagingData
import co.kr.mvisample.data.model.Pokemon
import co.kr.mvisample.data.model.PokemonDetail
import co.kr.mvisample.data.model.PokemonIcon
import co.kr.mvisample.data.model.Type
import co.kr.mvisample.data.repository.PokemonRepository
import co.kr.mvisample.data.result.Result
import co.kr.mvisample.data.resultMapper
import co.kr.mvisample.data.resultMapperWithLocal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakePokemonRepositoryUnitTest : PokemonRepository {

    private val pokemonIcons = MutableStateFlow(
        listOf(
            PokemonIcon(
                id = 6,
                iconUrl = "",
                order = 1
            ),
            PokemonIcon(
                id = 9,
                iconUrl = "",
                order = 2
            )
        )
    )

    override fun fetchPokemons(scope: CoroutineScope): Flow<PagingData<Pokemon>> = emptyFlow()

    override fun fetchPokemonDetail(id: Int, name: String): Flow<Result<PokemonDetail>> =
        resultMapperWithLocal(
            localAction = { PokemonDetail() },
            remoteAction = {
                PokemonDetail(
                    id = 6,
                    name = "charizard",
                    weight = 90.5f,
                    height = 1.7f,
                    types = listOf(
                        Type(
                            name = "fire"
                        ),
                        Type(
                            name = "flying"
                        )
                    )
                )
            }
        )

    override fun fetchPokemonIcons(): Flow<List<PokemonIcon>> = pokemonIcons.map { it.sortedBy { it.id } }

    override fun markAsDiscovered(id: Int): Flow<Result<Unit>> =
        resultMapper {}

    override fun markAsCaught(id: Int): Flow<Result<Unit>> =
        resultMapper {}

    override fun markAsRelease(id: Int): Flow<Result<Unit>> =
        resultMapper {}

    override fun swapPokemonOrder(firstId: Int, secondId: Int): Flow<Result<Unit>> =
        resultMapper {
            val first = pokemonIcons.value.find { it.id == firstId }
            val second = pokemonIcons.value.find { it.id == secondId }

            if (first != null && second != null) {
                pokemonIcons.update {
                    it.map { icon ->
                        when (icon.id) {
                            firstId -> icon.copy(order = second.order)
                            secondId -> icon.copy(order = first.order)
                            else -> icon
                        }
                    }
                }
            }

        }
}