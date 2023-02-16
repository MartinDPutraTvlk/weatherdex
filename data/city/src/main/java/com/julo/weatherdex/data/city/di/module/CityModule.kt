package com.julo.weatherdex.data.city.di.module

import com.julo.weatherdex.core.di.IoDispatcher
import com.julo.weatherdex.data.city.api.repository.CityRepository
import com.julo.weatherdex.data.city.implementation.remote.api.CityApi
import com.julo.weatherdex.data.city.implementation.repository.CityRepositoryImpl
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
object CityModule {

    private const val CITY_BASE_URL = "https://api.api-ninjas.com"
    private const val CITY_RETROFIT = "CITY_RETROFIT"

    @Named(CITY_RETROFIT)
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

    @Provides
    @Singleton
    fun provideCityApi(@Named(CITY_RETROFIT) retrofit: Retrofit): CityApi {
        return retrofit.create(CityApi::class.java)
    }

    @Provides
    @Singleton
    fun provideConsignmentCandidateRepository(
        cityApi: CityApi,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): CityRepository {
        return CityRepositoryImpl(
            cityApi = cityApi,
            ioDispatcher = ioDispatcher,
        )
    }
}
