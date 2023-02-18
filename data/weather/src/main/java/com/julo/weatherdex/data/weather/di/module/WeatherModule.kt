package com.julo.weatherdex.data.weather.di.module

import com.julo.weatherdex.core.di.IoDispatcher
import com.julo.weatherdex.data.weather.implementation.remote.api.WeatherApi
import com.julo.weatherdex.data.weather.implementation.repository.WeatherRepositoryImpl
import com.julo.weatherdex.data.weather.api.repository.WeatherRepository
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
object WeatherModule {

    private const val WEATHER_BASE_URL = "https://api.openweathermap.org"
    private const val WEATHER_RETROFIT = "WEATHER_RETROFIT"

    @Named(WEATHER_RETROFIT)
    @Singleton
    @Provides
    fun providesRetrofit(
        gsonConverterFactory: Converter.Factory,
        okHttpClient: OkHttpClient,
    ) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(WEATHER_BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherApi(@Named(WEATHER_RETROFIT) retrofit: Retrofit): WeatherApi {
        return retrofit.create(WeatherApi::class.java)
    }

    @Provides
    @Singleton
    fun provideConsignmentCandidateRepository(
        weatherApi: WeatherApi,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): WeatherRepository {
        return WeatherRepositoryImpl(
            weatherApi = weatherApi,
            ioDispatcher = ioDispatcher,
        )
    }
}
