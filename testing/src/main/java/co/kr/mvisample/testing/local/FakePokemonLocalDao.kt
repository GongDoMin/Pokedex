package co.kr.mvisample.testing.local

import co.kr.mvisample.local.model.PokemonLocalEntity
import co.kr.mvisample.local.room.dao.PokemonLocalDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
class FakePokemonLocalDao : PokemonLocalDao {

    private val pokemonLocals = hashMapOf<Int, PokemonLocalEntity>()

    override fun getPokemonLocals(isCaught: Boolean?): Flow<List<PokemonLocalEntity>> = flow {
        val filtered = when (isCaught) {
            null -> pokemonLocals.values
            else -> pokemonLocals.values.filter { it.isCaught == isCaught }
        }
        emit(filtered.toList())
    }

    override fun getPokemonLocal(id: Int): PokemonLocalEntity? =
        pokemonLocals[id]


    override suspend fun getMaxOrder(): Int =
        pokemonLocals.values.maxOf { it.order ?: 0 }

    override suspend fun swapPokemonOrder(id: Int, order: Int?) {
        val pokemon = pokemonLocals[id]?.copy(order = order)

        pokemon?.let { pokemonLocals[id] = it }
    }

    override suspend fun catchPokemon(id: Int, order: Int) {
        val pokemon = pokemonLocals[id]?.copy(isCaught = true, order = order)

        pokemon?.let { pokemonLocals[id] = it }
    }

    override suspend fun releasePokemon(id: Int) {
        val pokemon = pokemonLocals[id]?.copy(isCaught = false, order = null)

        pokemon?.let { pokemonLocals[id] = it }
    }

    override suspend fun markAsDiscovered(pokemon: PokemonLocalEntity) {
        pokemonLocals[pokemon.id] = pokemon
    }
}

