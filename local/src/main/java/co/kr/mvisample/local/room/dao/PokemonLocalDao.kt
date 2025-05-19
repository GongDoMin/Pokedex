package co.kr.mvisample.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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
    fun getPokemonLocal(id: Int): PokemonLocalEntity?

    @Query("SELECT MAX(`order`) FROM `pokemon-local`")
    suspend fun getMaxOrder(): Int?

    @Query("UPDATE `pokemon-local` SET `order` = :order WHERE id = :id")
    suspend fun swapPokemonOrder(id: Int, order: Int?)

    @Query("UPDATE `pokemon-local` SET isCaught = 1, `order` = :order WHERE id = :id")
    suspend fun catchPokemon(id: Int, order: Int)

    @Query("UPDATE `pokemon-local` SET isCaught = 0, `order` = NULL WHERE id = :id")
    suspend fun releasePokemon(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = PokemonLocalEntity::class)
    suspend fun markAsDiscovered(pokemon: PokemonLocalEntity)
}