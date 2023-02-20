package com.julo.weatherdex.data.weather.implementation.repository

import com.julo.weatherdex.base.network.http.HttpResponse
import com.julo.weatherdex.base.network.http.toError
import com.julo.weatherdex.data.weather.api.model.WeatherData
import com.julo.weatherdex.data.weather.api.repository.WeatherRepository
import com.julo.weatherdex.data.weather.implementation.mapper.toWeatherData
import com.julo.weatherdex.data.weather.implementation.remote.api.WeatherApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi,
    private val ioDispatcher: CoroutineDispatcher,
) : WeatherRepository {
    override suspend fun fetchWeatherData(lat: Double, long: Double): HttpResponse<WeatherData> {
        return withContext(ioDispatcher) {
            try {
                val response = weatherApi.fetchDailyWeather(
                    latitude = lat,
                    longitude = long,
                )
                if(response == null) {
                    HttpResponse.Empty
                } else {
                    HttpResponse.Success(response.toWeatherData())
                }
            } catch (e: Exception) {
                e.toError()
            }
        }
    }
}
