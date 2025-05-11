package co.kr.mvisample.local.di

import co.kr.mvisample.local.datasource.PokemonLocalDataSource
import co.kr.mvisample.local.datasource.RemoteKeyLocalDataSource
import co.kr.mvisample.local.impl.PokemonLocalDataSourceImpl
import co.kr.mvisample.local.impl.RemoteKeyLocalDataSourceImpl
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
    abstract fun bindPokemonLocalDataSource(pokemonLocalDataSourceImpl: PokemonLocalDataSourceImpl): PokemonLocalDataSource

    @Binds
    @Singleton
    abstract fun bindRemoteKeyLocalDataSource(remoteKeyLocalDataSourceImpl: RemoteKeyLocalDataSourceImpl): RemoteKeyLocalDataSource


}