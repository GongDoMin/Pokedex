package co.kr.mvisample.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import co.kr.mvisample.local.model.PokemonLocalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonLocalDao {
    @Query(
        """
            SELECT * FROM `pokemon-local` 
            WHERE (:isCaught IS NULL OR isCaught = :isCaught) 
            ORDER BY 
                CASE WHEN :isCaught = 1 THEN `order` ELSE `id` END ASC
        """
    )
    fun getPokemonLocals(isCaught: Boolean? = null): Flow<List<PokemonLocalEntity>>

    @Query("SELECT * FROM `pokemon-local` WHERE id = :id")
    fun getPokemonLocal(id: Int): PokemonLocalEntity

    @Query("SELECT MAX(`order`) FROM `pokemon-local`")
    suspend fun getMaxOrder(): Int?

    @Query("UPDATE `pokemon-local` SET isCaught = :isCaught, `order` = :order WHERE id = :id")
    suspend fun updatePokemonLocal(id: Int, isCaught: Boolean, order: Int?)

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = PokemonLocalEntity::class)
    suspend fun markAsDiscovered(pokemon: PokemonLocalEntity)

    @Transaction
    suspend fun swapPokemonOrder(firstId: Int, secondId: Int) {
        val firstPokemon = getPokemonLocal(firstId)
        val secondPokemon = getPokemonLocal(secondId)

        updatePokemonLocal(
            id = firstPokemon.id,
            isCaught = true,
            order = secondPokemon.order
        )
        updatePokemonLocal(
            id = secondPokemon.id,
            isCaught = true,
            order = firstPokemon.order
        )
    }
}