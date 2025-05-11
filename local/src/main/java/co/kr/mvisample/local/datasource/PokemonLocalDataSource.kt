package co.kr.mvisample.local.datasource

import androidx.paging.PagingSource
import co.kr.mvisample.local.model.PokemonEntity
import co.kr.mvisample.local.model.PokemonLocalEntity
import kotlinx.coroutines.flow.Flow

interface PokemonLocalDataSource {
    fun getPokemons(): PagingSource<Int, PokemonEntity>

    fun getPokemonLocals(): Flow<List<PokemonLocalEntity>>

    suspend fun insertPokemons(pokemons: List<PokemonEntity>)

    suspend fun getPokemon(id: Int): PokemonEntity

    suspend fun markAsDiscovered(id: Int)

    suspend fun markAsCaught(id: Int, isCaught: Boolean)

    suspend fun clearPokemons()
}