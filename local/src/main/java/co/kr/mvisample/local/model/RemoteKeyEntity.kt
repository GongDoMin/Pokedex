package co.kr.mvisample.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_key")
data class RemoteKeyEntity(
    @PrimaryKey val pokemonId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)
