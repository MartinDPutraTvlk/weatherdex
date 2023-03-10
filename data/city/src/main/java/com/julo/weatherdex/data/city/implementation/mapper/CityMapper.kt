package com.julo.weatherdex.data.city.implementation.mapper

import com.julo.weatherdex.core.extension.notNull
import com.julo.weatherdex.data.city.api.model.City
import com.julo.weatherdex.data.city.implementation.remote.response.CityEntity

fun Array<CityEntity>?.toCities() = this?.map {
    it.toCity()
}.notNull()
private fun CityEntity.toCity() = City(
    name = name.notNull(),
    latitude = latitude.notNull(),
    longitude = longitude.notNull(),
    country = country.notNull(),
    population = population.notNull(),
    isCapital = isCapital.notNull(),
)
