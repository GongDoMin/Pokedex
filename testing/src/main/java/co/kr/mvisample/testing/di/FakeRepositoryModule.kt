package co.kr.mvisample.testing.di

import co.kr.mvisample.data.di.RepositoryModule
import co.kr.mvisample.data.repository.PokemonRepository
import co.kr.mvisample.testing.data.FakePokemonRepository
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
        FakePokemonRepository()
}