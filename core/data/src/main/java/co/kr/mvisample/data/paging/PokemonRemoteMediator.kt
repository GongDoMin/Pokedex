package co.kr.mvisample.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import co.kr.mvisample.data.impl.LastPage
import co.kr.mvisample.data.impl.LoadSize
import co.kr.mvisample.data.impl.TotalLoadSize
import co.kr.mvisample.data.model.toData
import co.kr.mvisample.data.model.toEntity
import co.kr.mvisample.local.model.PokemonEntity
import co.kr.mvisample.local.room.dao.PokemonDao
import co.kr.mvisample.remote.datasource.PokemonDataSource

@OptIn(ExperimentalPagingApi::class)
class PokemonRemoteMediator(
    private val pokemonDataSource: PokemonDataSource,
    private val pokemonDao: PokemonDao,
    private val loadSize: Int = LoadSize,
    private val totalLoadSize: Int = TotalLoadSize,
    private val lastPage: Int = LastPage
) : RemoteMediator<Int, PokemonEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokemonEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                getKeyClosestToCurrentPosition(state) ?: 0
            }
            LoadType.PREPEND -> {
                val key = getKeyForFirstItem(state)
                if (key == null || key == 0) return MediatorResult.Success(endOfPaginationReached = true)
                else key.minus(1)
            }
            LoadType.APPEND -> {
                val key = getKeyForLastItem(state)
                if (key == null || key == lastPage) return MediatorResult.Success(endOfPaginationReached = true)
                else key.plus(1)
            }
        }

        try {
            val isLastPage = page == lastPage

            if (pokemonDao.getPokemonCount(page) != 0) {
                return MediatorResult.Success(endOfPaginationReached = isLastPage)
            }

            val limit = if (isLastPage) totalLoadSize - (loadSize * lastPage) else loadSize

            val response = pokemonDataSource.fetchPokemons(
                limit = limit,
                offset = page * loadSize
            )

            if (loadType == LoadType.REFRESH) {
                pokemonDao.clearPokemons()
            }

            val endOfPaginationReached = response.results.size < loadSize

            val pokemonEntities = response.results.map { it.toData().toEntity(page) }

            pokemonDao.insertPokemons(*pokemonEntities.toTypedArray())

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getKeyClosestToCurrentPosition(
        state: PagingState<Int, PokemonEntity>
    ) : Int? =
        state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                pokemonDao.getPokemon(id)?.page?.plus(1)
            }
        }

    private suspend fun getKeyForFirstItem(
        state: PagingState<Int, PokemonEntity>
    ) : Int? =
        state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { pokemon ->
                pokemonDao.getPokemon(pokemon.id)?.page
            }

    private suspend fun getKeyForLastItem(
        state: PagingState<Int, PokemonEntity>
    ): Int? =
        state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { pokemon ->
                pokemonDao.getPokemon(pokemon.id)?.page
            }
}