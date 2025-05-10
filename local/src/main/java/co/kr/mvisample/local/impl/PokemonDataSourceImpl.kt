package co.kr.mvisample.local.impl

import androidx.paging.PagingSource
import co.kr.mvisample.local.datasource.PokemonDataSource
import co.kr.mvisample.local.model.PokemonEntity
import co.kr.mvisample.local.room.dao.PokemonDao
import javax.inject.Inject

class PokemonDataSourceImpl @Inject constructor(
    private val pokemonDao: PokemonDao
) : PokemonDataSource {
    override fun getPokemons(): PagingSource<Int, PokemonEntity> =
        pokemonDao.getPokemons()

    override fun getPokemon(id: Int): PokemonEntity =
        pokemonDao.getPokemon(id)
}