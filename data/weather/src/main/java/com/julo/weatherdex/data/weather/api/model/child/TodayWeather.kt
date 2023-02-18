package com.julo.weatherdex.data.weather.api.model.child

data class TodayWeather(
    val dateInMillis: Long = 0,
    val humidity: Int = 0,
    val temp: Double = 0.0,
    val tempFeelsLike: Double = 0.0,
    val weather: Weather = Weather(),
)
