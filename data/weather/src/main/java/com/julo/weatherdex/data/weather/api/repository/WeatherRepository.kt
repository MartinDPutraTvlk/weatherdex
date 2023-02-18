package com.julo.weatherdex.data.weather.api.repository

import com.julo.weatherdex.base.network.http.HttpResponse
import com.julo.weatherdex.data.weather.api.model.WeatherData

interface WeatherRepository {
    /**
     * Fetch weather data from api service given the lat & long
     * */
    suspend fun fetchWeatherData(lat: Double, long: Double): HttpResponse<WeatherData>
}
