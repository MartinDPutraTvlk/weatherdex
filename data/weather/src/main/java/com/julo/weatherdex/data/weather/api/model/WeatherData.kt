package com.julo.weatherdex.data.weather.api.model

import com.julo.weatherdex.data.weather.api.model.child.ForecastWeather
import com.julo.weatherdex.data.weather.api.model.child.TodayWeather

data class WeatherData(
    val today: TodayWeather = TodayWeather(),
    val daily: List<ForecastWeather> = listOf(),
)
