package com.julo.weatherdex.data.cities.implementation.repository

import com.julo.weatherdex.base.network.http.HttpResponse
import com.julo.weatherdex.base.network.http.toError
import com.julo.weatherdex.data.cities.api.model.City
import com.julo.weatherdex.data.cities.api.repository.CitiesRepository
import com.julo.weatherdex.data.cities.implementation.mapper.toCity
import com.julo.weatherdex.data.cities.implementation.remote.api.CitiesApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class CitiesRepositoryImpl(
    private val citiesApi: CitiesApi,
    private val ioDispatcher: CoroutineDispatcher,
    private val apiKey: String,
) : CitiesRepository {
    override suspend fun fetchCityData(cityName: String): Flow<HttpResponse<City>> {
        return flow {
            emit(HttpResponse.Loading)
            try {
                val response = citiesApi.fetchCity(
                    token = apiKey,
                    name = cityName,
                )
                when (response) {
                    null -> HttpResponse.Empty
                    is HttpResponse.Success -> HttpResponse.Success(response.data.toCity())
                    else -> HttpResponse.Error("Unknown Error Occured")
                }
            } catch (e: Exception) {
                emit(e.toError())
            }
        }.flowOn(ioDispatcher)
    }
}
