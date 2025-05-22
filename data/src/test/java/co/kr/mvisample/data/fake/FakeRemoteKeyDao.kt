package co.kr.mvisample.data.fake

import co.kr.mvisample.local.model.RemoteKeyEntity
import co.kr.mvisample.local.room.dao.RemoteKeyDao

class FakeRemoteKeyDao : RemoteKeyDao {

    private val remoteKeys = mutableListOf<RemoteKeyEntity>()

    override suspend fun remoteKey(pokemonId: Int): RemoteKeyEntity? =
        remoteKeys.find { it.pokemonId == pokemonId }

    override suspend fun insertRemoteKeys(vararg remoteKeys: RemoteKeyEntity) {
        this.remoteKeys.addAll(remoteKeys)
    }

    override suspend fun clearRemoteKeys() {
        remoteKeys.clear()
    }
}