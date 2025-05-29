package co.kr.mvisample.local.di

import android.content.Context
import androidx.room.Room
import co.kr.mvisample.local.room.dao.PokemonDao
import co.kr.mvisample.local.room.dao.PokemonLocalDao
import co.kr.mvisample.local.room.database.PokemonDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun providePokemonDatabase(@ApplicationContext context: Context) : PokemonDatabase =
        Room.databaseBuilder(
            context,
            PokemonDatabase::class.java,
            "Pokemon.db"
        ).build()

    @Provides
    fun providePokemonDao(database: PokemonDatabase) : PokemonDao =
        database.pokemonDao()

    @Provides
    fun providePokemonLocalDao(database: PokemonDatabase) : PokemonLocalDao =
        database.pokemonLocalDao()
}