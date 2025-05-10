package co.kr.mvisample.local.datasource

import androidx.paging.PagingSource
import co.kr.mvisample.local.model.PokemonEntity

interface PokemonDataSource {
    fun getPokemons(): PagingSource<Int, PokemonEntity>

    fun getPokemon(id: Int): PokemonEntity

    suspend fun markAsDiscovered(id: Int)
}