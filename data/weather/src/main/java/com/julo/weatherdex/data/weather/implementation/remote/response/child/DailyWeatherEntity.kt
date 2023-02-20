package com.julo.weatherdex.data.weather.implementation.remote.response.child

import com.google.gson.annotations.SerializedName

data class DailyWeatherEntity(
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("icon")
    val icon: String? = null,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("main")
    val title: String? = null,
)
