package co.kr.mvisample.local.datasource

import co.kr.mvisample.local.model.RemoteKeyEntity

interface RemoteKeyLocalDataSource {
    suspend fun remoteKey(pokemonId: Int): RemoteKeyEntity?

    suspend fun insertRemoteKeys(remoteKeys: List<RemoteKeyEntity>)

    suspend fun clearRemoteKeys()
}