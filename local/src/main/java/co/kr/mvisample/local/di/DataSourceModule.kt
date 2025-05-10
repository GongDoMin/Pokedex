package co.kr.mvisample.local.di

import android.content.Context
import androidx.room.Room
import co.kr.mvisample.local.datasource.PokemonDataSource
import co.kr.mvisample.local.datasource.RemoteKeyDataSource
import co.kr.mvisample.local.impl.PokemonDataSourceImpl
import co.kr.mvisample.local.impl.RemoteKeyDataSourceImpl
import co.kr.mvisample.local.room.dao.PokemonDao
import co.kr.mvisample.local.room.dao.RemoteKeyDao
import co.kr.mvisample.local.room.database.PokemonDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Binds
    @Singleton
    abstract fun bindPokemonDataSource(pokemonDataSourceImpl: PokemonDataSourceImpl): PokemonDataSource

    @Binds
    @Singleton
    abstract fun bindRemoteKeyDataSource(remoteKeyDataSourceImpl: RemoteKeyDataSourceImpl): RemoteKeyDataSource


}