package co.kr.mvisample.data.di

import co.kr.mvisample.data.impl.PokemonRepositoryImpl
import co.kr.mvisample.data.repository.PokemonRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindPokemonRepository(pokemonRepositoryImpl: PokemonRepositoryImpl) : PokemonRepository
}