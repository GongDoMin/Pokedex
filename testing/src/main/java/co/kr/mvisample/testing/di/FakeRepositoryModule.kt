package co.kr.mvisample.testing.di

import co.kr.mvisample.data.di.RepositoryModule
import co.kr.mvisample.data.repository.PokemonRepository
import co.kr.mvisample.local.room.dao.PokemonDao
import co.kr.mvisample.local.room.dao.PokemonLocalDao
import co.kr.mvisample.local.room.dao.RemoteKeyDao
import co.kr.mvisample.remote.datasource.PokemonDataSource
import co.kr.mvisample.testing.data.FakePokemonRepository
import co.kr.mvisample.testing.local.FakePokemonDao
import co.kr.mvisample.testing.local.FakePokemonDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
class FakeRepositoryModule {
    @Provides
    @Singleton
    fun providePokemonRepository(
        pokemonDao: PokemonDao,
        pokemonLocalDao: PokemonLocalDao,
        remoteKeyDao: RemoteKeyDao
    ): PokemonRepository =
        FakePokemonRepository(
            pokemonDataSource = FakePokemonDataSource(),
            pokemonDao = pokemonDao,
            pokemonLocalDao = pokemonLocalDao,
            remoteKeyDao = remoteKeyDao
        )
}