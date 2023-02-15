package com.julo.weatherdex.data.cities.di.module

import com.julo.weatherdex.core.di.IoDispatcher
import com.julo.weatherdex.data.cities.implementation.remote.api.CitiesApi
import com.julo.weatherdex.data.cities.implementation.repository.CitiesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CitiesModule {

    private const val CITY_BASE_URL = "https://api.api-ninjas.com"
    private const val CITIES_RETROFIT = "CITIES_RETROFIT"
    private const val CITIES_API_KEY = "CITIES_API_KEY"


    @Named(CITIES_RETROFIT)
    @Singleton
    @Provides
    fun providesRetrofit(
        gsonConverterFactory: Converter.Factory,
        okHttpClient: OkHttpClient,
    ) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(CITY_BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()
    }

    @Named(CITIES_API_KEY)
    @Singleton
    fun providesApiKey(): String = "1pDonunjt76TlAS4tPb4yg==JCWbLbLUhRGFzNJj"

    @Provides
    @Singleton
    fun provideCitiesApi(@Named(CITIES_RETROFIT) retrofit: Retrofit): CitiesApi {
        return retrofit.create(CitiesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideConsignmentCandidateRepository(
        citiesApi: CitiesApi,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        @Named(CITIES_API_KEY) apiKey: String,
    ): com.julo.weatherdex.data.cities.api.repository.CitiesRepository {
        return CitiesRepositoryImpl(
            citiesApi = citiesApi,
            ioDispatcher = ioDispatcher,
            apiKey = apiKey,
        )
    }
}
