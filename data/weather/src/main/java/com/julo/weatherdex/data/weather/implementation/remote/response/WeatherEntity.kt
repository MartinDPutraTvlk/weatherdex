package com.julo.weatherdex.data.weather.implementation.remote.response

import com.google.gson.annotations.SerializedName
import com.julo.weatherdex.data.weather.implementation.remote.response.child.CurrentEntity
import com.julo.weatherdex.data.weather.implementation.remote.response.child.DailyEntity

data class WeatherEntity(
    @SerializedName("current")
    val current: CurrentEntity?,
    @SerializedName("daily")
    val daily: List<DailyEntity>?,
    @SerializedName("lat")
    val lat: Double?,
    @SerializedName("lon")
    val lon: Double?,
    @SerializedName("timezone")
    val timezone: String?,
    @SerializedName("timezone_offset")
    val timezone_offset: Int?,
)
