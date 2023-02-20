package com.julo.weatherdex.data.weather.implementation.remote.response

import com.google.gson.annotations.SerializedName
import com.julo.weatherdex.data.weather.implementation.remote.response.child.CurrentEntity
import com.julo.weatherdex.data.weather.implementation.remote.response.child.DailyEntity

data class WeatherEntity(
    @SerializedName("current")
    val current: CurrentEntity? = null,
    @SerializedName("daily")
    val daily: List<DailyEntity>? = null,
    @SerializedName("lat")
    val lat: Double? = null,
    @SerializedName("lon")
    val lon: Double? = null,
    @SerializedName("timezone")
    val timezone: String? = null,
    @SerializedName("timezone_offset")
    val timezone_offset: Int? = null,
)
