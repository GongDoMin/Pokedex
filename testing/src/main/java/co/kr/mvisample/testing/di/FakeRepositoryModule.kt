package co.kr.mvisample.testing.di

import co.kr.mvisample.data.di.RepositoryModule
import co.kr.mvisample.data.impl.PokemonRepositoryImpl
import co.kr.mvisample.data.repository.PokemonRepository
import co.kr.mvisample.testing.local.FakePokemonDao
import co.kr.mvisample.testing.local.FakePokemonDataSource
import co.kr.mvisample.testing.local.FakePokemonLocalDao
import co.kr.mvisample.testing.local.FakeRemoteKeyDao
import dagger.Binds
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
    fun providePokemonRepository(): PokemonRepository =
        PokemonRepositoryImpl(
            pokemonDataSource = FakePokemonDataSource(),
            pokemonDao = FakePokemonDao(),
            pokemonLocalDao = FakePokemonLocalDao(),
            remoteKeyDao = FakeRemoteKeyDao()
        )
}