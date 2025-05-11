package co.kr.mvisample.local.impl

import co.kr.mvisample.local.datasource.RemoteKeyLocalDataSource
import co.kr.mvisample.local.model.RemoteKeyEntity
import co.kr.mvisample.local.room.dao.RemoteKeyDao
import javax.inject.Inject

class RemoteKeyLocalDataSourceImpl @Inject constructor(
    private val remoteKeyDao: RemoteKeyDao
) : RemoteKeyLocalDataSource {
    override suspend fun remoteKey(pokemonId: Int): RemoteKeyEntity? =
        remoteKeyDao.remoteKey(pokemonId)

    override suspend fun insertRemoteKeys(remoteKeys: List<RemoteKeyEntity>) =
        remoteKeyDao.insertRemoteKeys(remoteKeys)

    override suspend fun clearRemoteKeys() =
        remoteKeyDao.clearRemoteKeys()
}