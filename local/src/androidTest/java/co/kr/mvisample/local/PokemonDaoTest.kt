package co.kr.mvisample.local

import androidx.test.ext.junit.runners.AndroidJUnit4
import co.kr.mvisample.local.model.PokemonEntity
import co.kr.mvisample.local.room.dao.PokemonDao
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PokemonDaoTest : PokemonDatabaseTest() {

    private lateinit var pokemonDao: PokemonDao

    @Before
    fun setUp() {
        setupDatabase()
        pokemonDao = pokemonDatabase.pokemonDao()
    }

    @After
    fun tearDown() {
        closeDatabase()
    }

    @Test
    fun 포켓몬을추가하고_id로조회하면_포켓몬을반환한다() = runTest {
        // given
        val expected = Pokemon.first()

        // when
        pokemonDao.insertPokemons(Pokemon)
        val result = pokemonDao.getPokemon(expected.id)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun 모든포켓몬을삭제하면_조회결과가_null이다() = runTest {
        // given
        pokemonDao.insertPokemons(Pokemon)

        // when
        pokemonDao.clearPokemons()
        val result = pokemonDao.getPokemon(1)

        // then
        assertNull(result)
    }

    companion object {
        private val Pokemon = listOf(
            PokemonEntity(id = 1, name = "이상해씨", imgUrl = ""),
            PokemonEntity(id = 2, name = "이상해풀", imgUrl = ""),
            PokemonEntity(id = 3, name = "이상해꽃", imgUrl = "")
        )
    }
}