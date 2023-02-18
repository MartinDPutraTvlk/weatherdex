package com.julo.weatherdex.data.weather.implementation.remote.api

import com.julo.weatherdex.data.weather.implementation.remote.response.WeatherEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/3.0/onecall")
    suspend fun fetchDailyWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String = "8a92cd8a28269ef8957f5e8411d6d88e",
        @Query("exclude") excludedParams: String = "minutely,hourly,alert",
        @Query("units") units: String = "metric",
    ): WeatherEntity?
}
