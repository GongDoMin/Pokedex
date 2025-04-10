package co.kr.mvisample.domain.di

import co.kr.mvisample.domain.usecase.GetUserUseCase
import co.kr.mvisample.domain.usecase.PlusNumberUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DomainModule {
    @Provides
    fun provideGetUserUseCase(): GetUserUseCase = GetUserUseCase()

    @Provides
    fun providePlusNumberUseCase(): PlusNumberUseCase = PlusNumberUseCase()
}