package co.kr.mvisample.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import co.kr.mvisample.data.impl.LoadSize
import co.kr.mvisample.data.model.Pokemon
import co.kr.mvisample.data.model.toData
import co.kr.mvisample.data.model.toEntity
import co.kr.mvisample.local.model.PokemonEntity
import co.kr.mvisample.local.model.RemoteKeyEntity
import co.kr.mvisample.local.room.dao.PokemonDao
import co.kr.mvisample.local.room.dao.RemoteKeyDao
import co.kr.mvisample.remote.datasource.PokemonDataSource

@OptIn(ExperimentalPagingApi::class)
class PokemonRemoteMediator(
    private val pokemonDataSource: PokemonDataSource,
    private val pokemonDao: PokemonDao,
    private val remoteKeyDao: RemoteKeyDao
) : RemoteMediator<Int, PokemonEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokemonEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKey = getRemoteKeyClosestToCurrentPosition(state)
                remoteKey?.nextKey?.minus(1) ?: 0
            }
            LoadType.PREPEND -> {
                val remoteKey = getRemoteKeyForFirstItem(state)
                remoteKey?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
            }
            LoadType.APPEND -> {
                val remoteKey = getRemoteKeyForLastItem(state)
                val nextKey = remoteKey?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
                nextKey
            }
        }

        try {
            val response = pokemonDataSource.fetchPokemons(
                limit = LoadSize,
                offset = page * LoadSize
            )

            if (loadType == LoadType.REFRESH) {
                remoteKeyDao.clearRemoteKeys()
                pokemonDao.clearPokemons()
            }

            val endOfPaginationReached = response.next.isEmpty()
            val prevKey = if (page == 0) null else page - 1
            val nextKey = if (endOfPaginationReached) null else page + 1

            val pokemonEntities = response.results.map { it.toData().toEntity() }
            val keys = pokemonEntities.map {
                RemoteKeyEntity(pokemonId = it.id, prevKey = prevKey, nextKey = nextKey)
            }

            remoteKeyDao.insertRemoteKeys(keys)
            pokemonDao.insertPokemons(pokemonEntities)

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, PokemonEntity>
    ): RemoteKeyEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                remoteKeyDao.remoteKey(id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, PokemonEntity>
    ): RemoteKeyEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { pokemon ->
                remoteKeyDao.remoteKey(pokemon.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, PokemonEntity>
    ): RemoteKeyEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { pokemon ->
                remoteKeyDao.remoteKey(pokemon.id)
            }
    }
}