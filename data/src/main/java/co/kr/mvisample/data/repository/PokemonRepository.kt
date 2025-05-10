package co.kr.mvisample.data.repository

import androidx.paging.PagingData
import co.kr.mvisample.data.model.Pokemon
import co.kr.mvisample.data.model.PokemonDetail
import co.kr.mvisample.data.result.Result
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    fun fetchPokemons(): Flow<PagingData<Pokemon>>

    fun fetchPokemonDetail(id: Int, name: String): Flow<Result<PokemonDetail>>
}