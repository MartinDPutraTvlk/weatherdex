package com.julo.weatherdex.data.weather.api.model.child

data class ForecastWeather(
    val dateInMillis: Long = 0,
    val humidity: Int = 0,
    val windSpeed: Double = 0.0,
    val temps: Map<String, Double> = emptyMap(),
    val feelsLike: Map<String, Double> = emptyMap(),
    val weather: Weather = Weather()
)
