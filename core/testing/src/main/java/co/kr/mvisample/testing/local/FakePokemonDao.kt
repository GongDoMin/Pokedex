package co.kr.mvisample.testing.local

import androidx.paging.PagingSource
import androidx.paging.PagingState
import co.kr.mvisample.local.model.PokemonEntity
import co.kr.mvisample.local.room.dao.PokemonDao

class FakePokemonDao : PokemonDao {

    private val pokemons = mutableListOf<PokemonEntity>()

    override fun getPokemons(): PagingSource<Int, PokemonEntity> =
        object : PagingSource<Int, PokemonEntity>() {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonEntity> {
                val key = params.key ?: 0
                val pageSize = params.loadSize
                val startIndex = key * pageSize
                val endIndex = minOf(startIndex + pageSize, pokemons.size)

                return LoadResult.Page(
                    data = pokemons.subList(startIndex, endIndex),
                    prevKey = if (key == 0) null else key - 1,
                    nextKey = if (endIndex < pokemons.size) null else key + 1
                )
            }

            override fun getRefreshKey(state: PagingState<Int, PokemonEntity>): Int? {
                return state.anchorPosition?.let { anchorPosition ->
                    state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                        ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
                }
            }
        }

    override suspend fun getPokemon(id: Int): PokemonEntity? =
        pokemons.find { it.id == id }

    override suspend fun getPokemonCount(page: Int): Int =
        pokemons.filter { it.page == page }.size

    override suspend fun insertPokemons(vararg pokemons: PokemonEntity) {
        val newList = pokemons.filterNot { pokemon -> this.pokemons.any { it.id == pokemon.id } }
        this.pokemons.addAll(newList)
    }

    override suspend fun clearPokemons() {
        pokemons.clear()
    }
}