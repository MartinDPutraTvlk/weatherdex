package com.julo.weatherdex.data.cities.api.repository

import com.julo.weatherdex.base.network.http.HttpResponse
import com.julo.weatherdex.data.cities.api.model.City
import kotlinx.coroutines.flow.Flow

interface CitiesRepository {
    /**
     * Fetch a city data from api service given the city name
     * */
    suspend fun fetchCityData(cityName: String): Flow<HttpResponse<City>>
}
