package co.kr.mvisample.data.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import co.kr.mvisample.data.model.Pokemon
import co.kr.mvisample.data.model.PokemonDetail
import co.kr.mvisample.data.model.toData
import co.kr.mvisample.data.paging.PokemonPagingSource
import co.kr.mvisample.data.repository.PokemonRepository
import co.kr.mvisample.data.resultMapper
import co.kr.mvisample.remote.datasource.PokemonDataSource
import co.kr.mvisample.data.result.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val pokemonDataSource : PokemonDataSource
): PokemonRepository {
    override fun fetchPokemons(): Flow<PagingData<Pokemon>> =
        Pager(
            config = PagingConfig(
                pageSize = LoadSize,
                initialLoadSize = InitialLoadSize
            ),
            pagingSourceFactory = { PokemonPagingSource(pokemonDataSource) }
        ).flow

    override fun fetchPokemonDetail(name: String): Flow<Result<PokemonDetail>> =
        resultMapper {
            pokemonDataSource.fetchPokemonDetail(name).toData()
        }
}

const val LoadSize = 100
const val InitialLoadSize = 100