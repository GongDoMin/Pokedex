package co.kr.mvisample.local.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import co.kr.mvisample.local.model.PokemonEntity
import co.kr.mvisample.local.model.PokemonLocalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {
    @Query("SELECT * FROM pokemon")
    fun getPokemons(): PagingSource<Int, PokemonEntity>

    @Query("SELECT * FROM `pokemon-local`")
    fun getPokemonLocals(): Flow<List<PokemonLocalEntity>>

    @Query("SELECT * FROM `pokemon-local` WHERE isCaught = 1 ORDER BY `order` ASC")
    fun getCaughtPokemons(): Flow<List<PokemonLocalEntity>>

    @Query("SELECT * FROM `pokemon-local` WHERE id = :id")
    fun getPokemonLocal(id: Int): PokemonLocalEntity

    @Query("SELECT * FROM pokemon WHERE id = :id")
    suspend fun getPokemon(id: Int): PokemonEntity

    @Query("SELECT MAX(`order`) FROM `pokemon-local`")
    suspend fun getMaxOrder(): Int?

    @Query("UPDATE `pokemon-local` SET isCaught = :isCaught, `order` = :order WHERE id = :id")
    suspend fun updatePokemon(id: Int, isCaught: Boolean, order: Int?)

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = PokemonLocalEntity::class)
    suspend fun markAsDiscovered(pokemon: PokemonLocalEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemons(pokemons: List<PokemonEntity>)

    @Query("DELETE FROM pokemon")
    suspend fun clearPokemons()

    @Transaction
    suspend fun swapPokemonOrder(firstId: Int, secondId: Int) {
        val firstPokemon = getPokemonLocal(firstId)
        val secondPokemon = getPokemonLocal(secondId)

        updatePokemon(
            id = firstPokemon.id,
            isCaught = true,
            order = secondPokemon.order
        )
        updatePokemon(
            id = secondPokemon.id,
            isCaught = true,
            order = firstPokemon.order
        )
    }
}