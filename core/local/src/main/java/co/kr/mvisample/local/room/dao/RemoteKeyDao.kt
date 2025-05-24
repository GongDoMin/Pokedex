package co.kr.mvisample.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import co.kr.mvisample.local.model.RemoteKeyEntity

@Dao
interface RemoteKeyDao {
    @Query("SELECT * FROM remote_key WHERE pokemonId = :pokemonId")
    suspend fun remoteKey(pokemonId: Int): RemoteKeyEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRemoteKeys(vararg remoteKeys: RemoteKeyEntity)

    @Query("DELETE FROM remote_key")
    suspend fun clearRemoteKeys()
}