package com.julo.weatherdex.data.cities.implementation.mapper

import com.julo.weatherdex.core.extension.notNull
import com.julo.weatherdex.data.cities.api.model.City
import com.julo.weatherdex.data.cities.implementation.remote.response.CityEntity

fun CityEntity.toCity() = City(
    name = name.notNull(),
    latitude = latitude.notNull(),
    longitude = longitude.notNull(),
    country = country.notNull(),
    population = population.notNull(),
    isCapital = isCapital.notNull(),
)
