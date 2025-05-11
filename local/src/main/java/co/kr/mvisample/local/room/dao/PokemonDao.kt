package co.kr.mvisample.local.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import co.kr.mvisample.local.model.PokemonEntity
import co.kr.mvisample.local.model.PokemonLocalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {
    @Query("SELECT * FROM pokemon")
    fun getPokemons(): PagingSource<Int, PokemonEntity>

    @Query("SELECT * FROM `pokemon-local`")
    fun getPokemonLocals(): Flow<List<PokemonLocalEntity>>

    @Query("SELECT * FROM pokemon WHERE id = :id")
    suspend fun getPokemon(id: Int): PokemonEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = PokemonLocalEntity::class)
    suspend fun markAsDiscovered(pokemon: PokemonLocalEntity)

    @Query("UPDATE `pokemon-local` SET isCaught = :isCaught, `order` = :order WHERE id = :id")
    suspend fun markAsCaught(id: Int, isCaught: Boolean, order: Int?)

    @Query("SELECT MAX(`order`) FROM `pokemon-local`")
    suspend fun getMaxOrder(): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemons(pokemons: List<PokemonEntity>)

    @Query("DELETE FROM pokemon")
    suspend fun clearPokemons()
}