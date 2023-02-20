package com.julo.weatherdex.data.weather.implementation.remote.response.child

import com.google.gson.annotations.SerializedName

data class DailyEntity(
    @SerializedName("clouds")
    val clouds: Int? = null,
    @SerializedName("dew_point")
    val dewPoint: Double? = null,
    @SerializedName("dt")
    val dt: Int? = null,
    @SerializedName("feels_like")
    val feelsLike: FeelsLikeEntity? = null,
    @SerializedName("humidity")
    val humidity: Int? = null,
    @SerializedName("moon_phase")
    val moonPhase: Double? = null,
    @SerializedName("moonrise")
    val moonrise: Int? = null,
    @SerializedName("moonset")
    val moonset: Int? = null,
    @SerializedName("pop")
    val pop: Double? = null,
    @SerializedName("pressure")
    val pressure: Int? = null,
    @SerializedName("rain")
    val rain: Double? = null,
    @SerializedName("sunrise")
    val sunrise: Int? = null,
    @SerializedName("sunset")
    val sunset: Int? = null,
    @SerializedName("temp")
    val temp: TempEntity? = null,
    @SerializedName("uvi")
    val uvi: Double? = null,
    @SerializedName("weather")
    val weather: List<DailyWeatherEntity>? = null,
    @SerializedName("wind_deg")
    val windDeg: Int? = null,
    @SerializedName("wind_gust")
    val windGust: Double? = null,
    @SerializedName("wind_speed")
    val windSpeed: Double? = null,
)
