package com.julo.weatherdex.data.weather.implementation.remote.response.child

import com.google.gson.annotations.SerializedName

data class DailyWeatherEntity(
    @SerializedName("description")
    val description: String?,
    @SerializedName("icon")
    val icon: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("main")
    val title: String?,
)
