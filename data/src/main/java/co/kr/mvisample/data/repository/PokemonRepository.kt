package co.kr.mvisample.data.repository

import androidx.paging.PagingData
import co.kr.mvisample.data.model.Pokemon
import co.kr.mvisample.data.model.PokemonDetail
import co.kr.mvisample.data.model.PokemonIcon
import co.kr.mvisample.data.result.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    fun fetchPokemons(scope: CoroutineScope): Flow<PagingData<Pokemon>>

    fun fetchPokemonDetail(id: Int, name: String): Flow<Result<PokemonDetail>>

    fun fetchPokemonIcons(): Flow<List<PokemonIcon>>

    fun markAsDiscovered(id: Int): Flow<Result<Unit>>

    fun markAsCaught(id: Int, isCaught: Boolean): Flow<Result<Unit>>

    fun swapPokemonOrder(firstId: Int, secondId: Int): Flow<Result<Unit>>
}