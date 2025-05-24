package co.kr.mvisample.local

import androidx.test.ext.junit.runners.AndroidJUnit4
import co.kr.mvisample.local.model.RemoteKeyEntity
import co.kr.mvisample.local.room.dao.RemoteKeyDao
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RemoteKeyDaoTest : PokemonDatabaseTest() {

    private lateinit var remoteKeyDao: RemoteKeyDao

    @Before
    fun setUp() {
        setupDatabase()
        remoteKeyDao = pokemonDatabase.remoteKeyDao()
    }

    @After
    fun tearDown() {
        closeDatabase()
    }

    @Test
    fun 특정포켓몬의_remoteKey를_조회한다() = runTest {
        // given
        val expected = RemoteKeyEntity(pokemonId = 1, prevKey = null, nextKey = 2)
        remoteKeyDao.insertRemoteKeys(expected)

        // when
        val result = remoteKeyDao.remoteKey(expected.pokemonId)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun remoteKey가_없으면_null을_반환한다() = runTest {
        // when
        val result = remoteKeyDao.remoteKey(999)

        // then
        assertNull(result)
    }

    @Test
    fun remoteKey_목록을_삽입한다() = runTest {
        // given
        val expectedRemoteKeys = listOf(
            RemoteKeyEntity(pokemonId = 1, prevKey = null, nextKey = 2),
            RemoteKeyEntity(pokemonId = 2, prevKey = 1, nextKey = 3),
            RemoteKeyEntity(pokemonId = 3, prevKey = 2, nextKey = null)
        )

        // when
        remoteKeyDao.insertRemoteKeys(*expectedRemoteKeys.toTypedArray())

        // then
        for (expected in expectedRemoteKeys) {
            val result = remoteKeyDao.remoteKey(expected.pokemonId)
            assertEquals(expected, result)
        }
    }

    @Test
    fun remoteKey_전체를_삭제한다() = runTest {
        // given
        val expectedRemoteKeys = listOf(
            RemoteKeyEntity(pokemonId = 1, prevKey = null, nextKey = 2),
            RemoteKeyEntity(pokemonId = 2, prevKey = 1, nextKey = 3)
        )
        remoteKeyDao.insertRemoteKeys(*expectedRemoteKeys.toTypedArray())

        // when
        remoteKeyDao.clearRemoteKeys()

        // then
        for (key in expectedRemoteKeys) {
            val result = remoteKeyDao.remoteKey(key.pokemonId)
            assertNull(result)
        }
    }
}