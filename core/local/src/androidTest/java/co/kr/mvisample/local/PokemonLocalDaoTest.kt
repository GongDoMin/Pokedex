package co.kr.mvisample.local

import androidx.test.ext.junit.runners.AndroidJUnit4
import co.kr.mvisample.local.model.PokemonEntity
import co.kr.mvisample.local.model.PokemonLocalEntity
import co.kr.mvisample.local.room.dao.PokemonDao
import co.kr.mvisample.local.room.dao.PokemonLocalDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PokemonLocalDaoTest : PokemonDatabaseTest() {

    private lateinit var pokemonDao: PokemonDao
    private lateinit var pokemonLocalDao: PokemonLocalDao

    @Before
    fun setUp() = runTest {
        setupDatabase()
        pokemonDao = pokemonDatabase.pokemonDao()
        pokemonDao.insertPokemons(
            * Pokemons.map { PokemonEntity(id = it.id) }.toTypedArray()
        )
        pokemonLocalDao = pokemonDatabase.pokemonLocalDao()
    }

    @After
    fun tearDown() {
        closeDatabase()
    }

    @Test
    fun 발견한_포켓몬을_반환한다() = runTest {
        // given
        Pokemons.forEach { pokemonLocalDao.markAsDiscovered(it) }

        // when
        val result = pokemonLocalDao.getPokemonLocals(null).first()

        // then
        assertEquals(Pokemons, result)
    }

    @Test
    fun 포획한_포켓몬을_반환한다() = runTest {
        // given
        val expected = Pokemons.filter { it.isCaught }
        Pokemons.forEach { pokemonLocalDao.markAsDiscovered(it) }

        // when
        val result = pokemonLocalDao.getPokemonLocals(true).first()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun 아이디로_포켓몬을_반환한다() = runTest {
        // given
        val expected = Pokemons.first { it.id == 3 }
        Pokemons.forEach { pokemonLocalDao.markAsDiscovered(it) }

        // when
        val result = pokemonLocalDao.getPokemonLocal(expected.id)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun 포획한_포켓몬중_가장큰_order를_반환한다() = runTest {
        // given
        val expected = Pokemons.maxOf { it.order ?: 0 }
        Pokemons.forEach { pokemonLocalDao.markAsDiscovered(it) }

        // when
        val result = pokemonLocalDao.getMaxOrder()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun 포켓몬을_포획한다() = runTest {
        // given
        val target = Pokemons.first { !it.isCaught }
        val expectedOrder = Pokemons.maxOf { it.order ?: 0 }.plus(1)
        Pokemons.forEach { pokemonLocalDao.markAsDiscovered(it) }

        // when
        pokemonLocalDao.catchPokemon(id = target.id, order = expectedOrder)
        val result = pokemonLocalDao.getPokemonLocal(target.id)

        // then
        assertTrue(result?.isCaught == true)
        assertEquals(expectedOrder, result?.order)
    }

    @Test
    fun 포켓몬을_놓아준다() = runTest {
        // given
        val target = Pokemons.first { it.isCaught }
        Pokemons.forEach { pokemonLocalDao.markAsDiscovered(it) }

        // when
        pokemonLocalDao.releasePokemon(id = target.id)
        val result = pokemonLocalDao.getPokemonLocal(target.id)

        // then
        assertFalse(result?.isCaught == true)
        assertNull(result?.order)
    }

    @Test
    fun 포켓몬을_발견하면_DB에_저장된다() = runTest {
        // given
        val expected = PokemonLocalEntity(id = 1, iconUrl = "", isCaught = false, order = null)

        // when
        pokemonLocalDao.markAsDiscovered(expected)
        val result = pokemonLocalDao.getPokemonLocal(expected.id)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun 포켓몬_순서를_변경한다() = runTest {
        // given
        val firstExpected = Pokemons.filter { it.isCaught }.random()
        val secondExpected = Pokemons.filter { it.isCaught }.random()
        Pokemons.forEach { pokemonLocalDao.markAsDiscovered(it) }

        // when
        pokemonLocalDao.swapPokemonOrder(firstExpected.id, secondExpected.order)
        pokemonLocalDao.swapPokemonOrder(secondExpected.id, firstExpected.order)
        val firstResult = pokemonLocalDao.getPokemonLocal(firstExpected.id)
        val secondResult = pokemonLocalDao.getPokemonLocal(secondExpected.id)

        // then
        assertEquals(firstExpected.order, secondResult?.order)
        assertEquals(secondExpected.order, firstResult?.order)
    }

    companion object {
        private val Pokemons = listOf(
            PokemonLocalEntity(1, "", true, 1),
            PokemonLocalEntity(2, "", false, null),
            PokemonLocalEntity(3, "", true, 2),
            PokemonLocalEntity(4, "", true, 3),
            PokemonLocalEntity(5, "", false, null),
            PokemonLocalEntity(6, "", true, 4)
        )
    }
}