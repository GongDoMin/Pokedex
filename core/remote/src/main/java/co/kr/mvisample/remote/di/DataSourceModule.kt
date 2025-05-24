package co.kr.mvisample.remote.di

import co.kr.mvisample.remote.datasource.PokemonDataSource
import co.kr.mvisample.remote.impl.PokemonDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Binds
    @Singleton
    abstract fun bindPokemonDataSource(pokemonDataSourceImpl: PokemonDataSourceImpl): PokemonDataSource
}