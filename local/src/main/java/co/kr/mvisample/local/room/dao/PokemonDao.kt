package co.kr.mvisample.local.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import co.kr.mvisample.local.model.PokemonEntity

@Dao
interface PokemonDao {
    @Query("SELECT * FROM pokemon")
    fun getPokemons(): PagingSource<Int, PokemonEntity>
}