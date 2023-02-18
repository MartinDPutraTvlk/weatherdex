package com.julo.weatherdex.data.weather.implementation.remote.response.child

import com.google.gson.annotations.SerializedName

data class FeelsLikeEntity(
    @SerializedName("morn")
    val morning: Double?,
    @SerializedName("day")
    val day: Double?,
    @SerializedName("eve")
    val evening: Double?,
    @SerializedName("night")
    val night: Double?,
)
