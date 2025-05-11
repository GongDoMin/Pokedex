package co.kr.mvisample.local.impl

import androidx.paging.PagingSource
import co.kr.mvisample.local.datasource.PokemonLocalDataSource
import co.kr.mvisample.local.model.PokemonEntity
import co.kr.mvisample.local.model.PokemonLocalEntity
import co.kr.mvisample.local.room.dao.PokemonDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PokemonLocalDataSourceImpl @Inject constructor(
    private val pokemonDao: PokemonDao
) : PokemonLocalDataSource {
    override fun getPokemons(): PagingSource<Int, PokemonEntity> =
        pokemonDao.getPokemons()

    override fun getPokemonLocals(): Flow<List<PokemonLocalEntity>> =
        pokemonDao.getPokemonLocals()

    override suspend fun insertPokemons(pokemons: List<PokemonEntity>) {
        pokemonDao.insertPokemons(pokemons)
    }

    override suspend fun getPokemon(id: Int): PokemonEntity =
        pokemonDao.getPokemon(id)

    override suspend fun markAsDiscovered(id: Int) {
        pokemonDao.markAsDiscovered(
            PokemonLocalEntity(
                id = id,
                iconUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-vii/icons/$id.png",
                isCaught = false,
                order = null
            )
        )
    }

    override suspend fun markAsCaught(id: Int, isCaught: Boolean) {
        val order = if (isCaught) pokemonDao.getMaxOrder()?.plus(1) ?: 0 else null
        pokemonDao.markAsCaught(
            id = id,
            isCaught = isCaught,
            order = order
        )
    }

    override suspend fun clearPokemons() {
        pokemonDao.clearPokemons()
    }
}