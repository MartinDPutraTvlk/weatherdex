package com.julo.weatherdex.data.cities.implementation.remote.response

import com.google.gson.annotations.SerializedName

data class CityEntity(
    @SerializedName("name") val name: String?,
    @SerializedName("latitude") val latitude: Double?,
    @SerializedName("longitude") val longitude: Double?,
    @SerializedName("country") val country: String?,
    @SerializedName("population") val population: Int?,
    @SerializedName("is_capital") val isCapital: Boolean?,
)
