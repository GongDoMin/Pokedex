package co.kr.mvisample.data.impl

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import co.kr.mvisample.data.model.Pokemon
import co.kr.mvisample.data.model.PokemonDetail
import co.kr.mvisample.data.model.toData
import co.kr.mvisample.data.paging.PokemonRemoteMediator
import co.kr.mvisample.data.repository.PokemonRepository
import co.kr.mvisample.data.result.Result
import co.kr.mvisample.data.resultMapper
import co.kr.mvisample.data.resultMapperWithLocal
import co.kr.mvisample.local.datasource.PokemonLocalDataSource
import co.kr.mvisample.local.datasource.RemoteKeyLocalDataSource
import co.kr.mvisample.remote.datasource.PokemonDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PokemonRepositoryImpl @Inject constructor(
    private val pokemonDataSource : PokemonDataSource,
    private val pokemonLocalDataSource: PokemonLocalDataSource,
    private val remoteKeyLocalDataSource: RemoteKeyLocalDataSource
): PokemonRepository {
    override fun fetchPokemons(scope: CoroutineScope): Flow<PagingData<Pokemon>> =
        Pager(
            config = PagingConfig(
                pageSize = LoadSize,
                initialLoadSize = InitialLoadSize
            ),
            remoteMediator = PokemonRemoteMediator(pokemonDataSource, pokemonLocalDataSource, remoteKeyLocalDataSource),
            pagingSourceFactory = { pokemonLocalDataSource.getPokemons() }
        ).flow.map { pagingData ->
            pagingData.map { pokemonEntity ->
                pokemonEntity.toData()
            }
        }.cachedIn(scope).combine(
            pokemonLocalDataSource.getPokemonLocals()
        ) { pagingData, pokemonLocals ->
            val localMap = pokemonLocals.associateBy { it.id }

            pagingData.map { pokemon ->
                localMap[pokemon.id]?.let {
                    pokemon.copy(
                        isDiscovered = pokemon.isDiscovered,
                        isCaught = pokemon.isCaught
                    )
                } ?: pokemon
            }
        }

    override fun fetchPokemonDetail(id: Int, name: String): Flow<Result<PokemonDetail>> =
        resultMapperWithLocal(
            localAction = {
                val entity = pokemonLocalDataSource.getPokemon(id)
                PokemonDetail(
                    id = entity.id,
                    name = entity.name,
                    imgUrl = entity.imgUrl
                )
            },
            remoteAction = {
                val response = pokemonDataSource.fetchPokemonDetail(name)
                PokemonDetail(
                    weight = response.weight * 0.1f,
                    height = response.height * 0.1f,
                    types = response.types.map { it.toData() }
                )
            }
        )

    override fun markAsDiscovered(id: Int): Flow<Result<Unit>> =
        resultMapper {
            pokemonLocalDataSource.markAsDiscovered(id)
        }
}

const val LoadSize = 100
const val TotalLoadSize = 251
const val InitialLoadSize = 100