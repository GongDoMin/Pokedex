package co.kr.mvisample.testing.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import co.kr.mvisample.data.impl.InitialLoadSize
import co.kr.mvisample.data.impl.LoadSize
import co.kr.mvisample.data.model.Pokemon
import co.kr.mvisample.data.model.PokemonDetail
import co.kr.mvisample.data.model.PokemonIcon
import co.kr.mvisample.data.model.Type
import co.kr.mvisample.data.model.toData
import co.kr.mvisample.data.model.toEntity
import co.kr.mvisample.data.paging.PokemonRemoteMediator
import co.kr.mvisample.data.repository.PokemonRepository
import co.kr.mvisample.data.result.Result
import co.kr.mvisample.data.resultMapper
import co.kr.mvisample.data.resultMapperWithLocal
import co.kr.mvisample.local.model.PokemonLocalEntity
import co.kr.mvisample.local.room.dao.PokemonDao
import co.kr.mvisample.local.room.dao.PokemonLocalDao
import co.kr.mvisample.remote.datasource.PokemonDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class FakePokemonRepository @Inject constructor(
    private val pokemonDataSource: PokemonDataSource,
    private val pokemonDao: PokemonDao,
    private val pokemonLocalDao: PokemonLocalDao
) : PokemonRepository {

    init {
        runBlocking {
            pokemonDao.insertPokemons(
                * pokemonDataSource.fetchPokemons(20, 0).results.map { it.toData().toEntity(1) }.toTypedArray()
            )
            pokemonLocalDao.markAsDiscovered(
                PokemonLocalEntity(
                    id = 6,
                    iconUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-vii/icons/6.png",
                    isCaught = true,
                    order = 0
                )
            )
            pokemonLocalDao.markAsDiscovered(
                PokemonLocalEntity(
                    id = 9,
                    iconUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-vii/icons/9.png",
                    isCaught = true,
                    order = 1
                )
            )
        }
    }

    override fun fetchPokemons(scope: CoroutineScope): Flow<PagingData<Pokemon>> =
        Pager(
            config = PagingConfig(
                pageSize = LoadSize,
                initialLoadSize = InitialLoadSize
            ),
            remoteMediator = PokemonRemoteMediator(pokemonDataSource, pokemonDao),
            pagingSourceFactory = { pokemonDao.getPokemons() }
        ).flow
            .map { pagingData ->
                pagingData.map {
                    it.toData()
                }
            }.cachedIn(scope).combine(
                pokemonLocalDao.getPokemonLocals(null)
            ) { pagingData, pokemonLocals ->
                val localMap = pokemonLocals.associateBy { it.id }

                pagingData.map { pokemon ->
                    localMap[pokemon.id]?.let {
                        pokemon.copy(
                            isDiscovered = true,
                            isCaught = it.isCaught
                        )
                    } ?: pokemon
                }
            }

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

    override fun fetchPokemonIcons(): Flow<List<PokemonIcon>> =
        pokemonLocalDao.getPokemonLocals(true).map { pokemonLocals ->
            pokemonLocals.map { it.toData() }
        }

    override fun markAsDiscovered(id: Int): Flow<Result<Unit>> =
        resultMapper {
            pokemonLocalDao.markAsDiscovered(
                PokemonLocalEntity(
                    id = id,
                    iconUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-vii/icons/$id.png",
                    isCaught = false,
                    order = null
                )
            )
        }

    override fun markAsCaught(id: Int): Flow<Result<Unit>> =
        resultMapper {
            val order = pokemonLocalDao.getMaxOrder()?.plus(1) ?: 0
            pokemonLocalDao.catchPokemon(
                id = id,
                order = order
            )
        }

    override fun markAsRelease(id: Int): Flow<Result<Unit>> =
        resultMapper {
            delay(500L)
            pokemonLocalDao.releasePokemon(
                id = id
            )
        }

    override fun swapPokemonOrder(firstId: Int, secondId: Int): Flow<Result<Unit>> =
        resultMapper {
            val firstPokemon = pokemonLocalDao.getPokemonLocal(firstId) ?: error("Pokemon with id $firstId not found.")
            val secondPokemon = pokemonLocalDao.getPokemonLocal(secondId) ?: error("Pokemon with id $secondId not found.")

            pokemonLocalDao.swapPokemonOrder(firstPokemon.id, secondPokemon.order)
            pokemonLocalDao.swapPokemonOrder(secondPokemon.id, firstPokemon.order)
        }
}