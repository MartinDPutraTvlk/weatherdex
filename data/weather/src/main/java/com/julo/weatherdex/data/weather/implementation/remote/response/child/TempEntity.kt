package com.julo.weatherdex.data.weather.implementation.remote.response.child

import com.google.gson.annotations.SerializedName

data class TempEntity(
    @SerializedName("morn")
    val morning: Double? = null,
    @SerializedName("day")
    val day: Double? = null,
    @SerializedName("eve")
    val evening: Double? = null,
    @SerializedName("night")
    val night: Double? = null,
    @SerializedName("min")
    val min: Double? = null,
    @SerializedName("max")
    val max: Double? = null,
)
