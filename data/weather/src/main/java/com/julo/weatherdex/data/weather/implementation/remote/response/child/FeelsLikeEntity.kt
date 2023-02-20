package com.julo.weatherdex.data.weather.implementation.remote.response.child

import com.google.gson.annotations.SerializedName

data class FeelsLikeEntity(
    @SerializedName("morn")
    val morning: Double? = null,
    @SerializedName("day")
    val day: Double? = null,
    @SerializedName("eve")
    val evening: Double? = null,
    @SerializedName("night")
    val night: Double? = null,
)
