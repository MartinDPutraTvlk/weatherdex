package com.julo.weatherdex.data.city.api.model

data class City(
    val name: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val country: String = "",
    val population: Int = 0,
    val isCapital: Boolean = false,
) {
    val isNotEmpty
        get() = this != EMPTY

    companion object {
        val EMPTY = City()
    }
}
