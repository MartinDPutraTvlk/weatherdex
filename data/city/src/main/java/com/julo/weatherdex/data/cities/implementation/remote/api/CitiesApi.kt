package com.julo.weatherdex.data.cities.implementation.remote.api

import com.julo.weatherdex.base.network.http.HttpResponse
import com.julo.weatherdex.data.cities.implementation.remote.response.CityEntity
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CitiesApi {
    @GET("v1/city?name=")
    suspend fun fetchCity(
        @Header("X-Api-Key") token: String,
        @Query("name") name: String,
    ): HttpResponse<CityEntity>?
}
