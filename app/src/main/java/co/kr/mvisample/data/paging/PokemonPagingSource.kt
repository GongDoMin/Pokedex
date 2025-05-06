package co.kr.mvisample.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import co.kr.mvisample.data.impl.LoadSize
import co.kr.mvisample.data.model.Pokemon
import co.kr.mvisample.data.model.toData
import co.kr.mvisample.remote.datasource.PokemonDataSource

class PokemonPagingSource(
    private val pokemonDataSource: PokemonDataSource,
): PagingSource<Int, Pokemon>() {
    override fun getRefreshKey(state: PagingState<Int, Pokemon>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> {
        val position = params.key ?: 0
        return try {
            val response = pokemonDataSource.fetchPokemons(
                limit = LoadSize,
                offset = position * LoadSize
            )
            val nextKey = if (response.next.isEmpty()) {
                null
            } else {
                position + (params.loadSize / LoadSize)
            }
            LoadResult.Page(
                data = response.results.map { it.toData() },
                prevKey = if (position == 0) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }
}