package co.kr.mvisample.remote.di

import co.kr.mvisample.remote.service.PokemonService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
internal class RemoteModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(timeout = 10, unit = TimeUnit.SECONDS)
            .readTimeout(timeout = 10, unit = TimeUnit.SECONDS)
            .writeTimeout(timeout = 10, unit = TimeUnit.SECONDS)
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BASIC
                }
            )
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient : OkHttpClient
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        val jsonConverterFactory = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }.asConverterFactory(contentType)

        return Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .client(okHttpClient)
            .addConverterFactory(jsonConverterFactory)
            .build()
    }


    @Provides
    @Singleton
    fun providePokemonService(
        retrofit: Retrofit
    ): PokemonService =
        retrofit.create(PokemonService::class.java)
}

