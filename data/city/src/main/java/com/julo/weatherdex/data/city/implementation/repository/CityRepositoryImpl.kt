package com.julo.weatherdex.data.city.implementation.repository

import com.julo.weatherdex.base.network.http.HttpResponse
import com.julo.weatherdex.base.network.http.toError
import com.julo.weatherdex.data.city.api.model.City
import com.julo.weatherdex.data.city.api.repository.CityRepository
import com.julo.weatherdex.data.city.implementation.mapper.toCities
import com.julo.weatherdex.data.city.implementation.remote.api.CityApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CityRepositoryImpl @Inject constructor(
    private val cityApi: CityApi,
    private val ioDispatcher: CoroutineDispatcher,
) : CityRepository {
    override suspend fun fetchCityData(cityName: String): HttpResponse<List<City>> {
        return withContext(ioDispatcher) {
            try {
                val response = cityApi.fetchCity(
                    name = cityName,
                )
                if(response == null || response.isEmpty()) {
                    HttpResponse.Empty
                } else {
                    HttpResponse.Success(
                        response.toCities()
                    )
                }
            } catch (e: Exception) {
                e.toError()
            }
        }
    }
}
