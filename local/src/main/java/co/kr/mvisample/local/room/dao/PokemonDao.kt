package co.kr.mvisample.local.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import co.kr.mvisample.local.model.PokemonEntity

@Dao
interface PokemonDao {
    @Query("SELECT * FROM pokemon")
    fun getPokemons(): PagingSource<Int, PokemonEntity>

    @Query("SELECT * FROM pokemon WHERE id = :id")
    suspend fun getPokemon(id: Int): PokemonEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemons(pokemons: List<PokemonEntity>)

    @Query("DELETE FROM pokemon")
    suspend fun clearPokemons()
}