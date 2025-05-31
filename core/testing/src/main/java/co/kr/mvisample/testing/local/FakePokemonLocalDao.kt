package co.kr.mvisample.testing.local

import co.kr.mvisample.local.model.PokemonLocalEntity
import co.kr.mvisample.local.room.dao.PokemonLocalDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakePokemonLocalDao : PokemonLocalDao {

    private val pokemonLocals = MutableStateFlow<List<PokemonLocalEntity>>(emptyList())

    override fun getPokemonLocals(isCaught: Boolean?): Flow<List<PokemonLocalEntity>> =
        pokemonLocals.map { list ->
            isCaught?.let { flag -> list.filter { it.isCaught == flag }.sortedBy { it.order } } ?: list
        }

    override fun getPokemonLocal(id: Int): PokemonLocalEntity? =
        pokemonLocals.value.find { it.id == id }

    override suspend fun getMaxOrder(): Int =
        pokemonLocals.value.maxOfOrNull { it.order ?: 0 } ?: 0

    override suspend fun swapPokemonOrder(id: Int, order: Int?) {
        updatePokemon(id) { it.copy(order = order) }
    }

    override suspend fun catchPokemon(id: Int, order: Int) {
        updatePokemon(id) { it.copy(isCaught = true, order = order) }
    }

    override suspend fun releasePokemon(id: Int) {
        updatePokemon(id) { it.copy(isCaught = false, order = null) }
    }

    override suspend fun markAsDiscovered(pokemon: PokemonLocalEntity) {
        pokemonLocals.update { current ->
            if (current.none { it.id == pokemon.id }) current + pokemon
            else current
        }
    }

    override suspend fun clearPokemonLocal() {
        pokemonLocals.value = emptyList()
    }

    private fun updatePokemon(id: Int, transform: (PokemonLocalEntity) -> PokemonLocalEntity) {
        pokemonLocals.update { list ->
            list.map { entity ->
                if (entity.id == id) transform(entity) else entity
            }
        }
    }
}

