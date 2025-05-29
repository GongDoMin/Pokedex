package co.kr.mvisample.testing.di

import android.content.Context
import androidx.room.Room
import co.kr.mvisample.local.di.DatabaseModule
import co.kr.mvisample.local.room.dao.PokemonDao
import co.kr.mvisample.local.room.dao.PokemonLocalDao
import co.kr.mvisample.local.room.database.PokemonDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
class FakeDataBaseModule {
    @Provides
    @Singleton
    fun providePokemonDatabase(@ApplicationContext context: Context) : PokemonDatabase =
        Room.databaseBuilder(
            context,
            PokemonDatabase::class.java,
            "Pokemon-test.db"
        ).build()

    @Provides
    fun providePokemonDao(database: PokemonDatabase) : PokemonDao =
        database.pokemonDao()

    @Provides
    fun providePokemonLocalDao(database: PokemonDatabase) : PokemonLocalDao =
        database.pokemonLocalDao()
}