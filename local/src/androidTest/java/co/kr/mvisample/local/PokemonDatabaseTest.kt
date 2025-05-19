package co.kr.mvisample.local

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import co.kr.mvisample.local.room.database.PokemonDatabase

open class PokemonDatabaseTest {
    protected lateinit var pokemonDatabase: PokemonDatabase

    protected fun setupDatabase() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        pokemonDatabase = Room.inMemoryDatabaseBuilder(context, PokemonDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    protected fun closeDatabase() {
        pokemonDatabase.close()
    }
}