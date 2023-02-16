package com.julo.weatherdex.data.city.api.repository

import com.julo.weatherdex.base.network.http.HttpResponse
import com.julo.weatherdex.data.city.api.model.City

interface CityRepository {
    /**
     * Fetch a city data from api service given the city name
     * */
    suspend fun fetchCityData(cityName: String): HttpResponse<City>
}
