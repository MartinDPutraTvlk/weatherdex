package com.julo.weatherdex.data.city.implementation.remote.api

import com.julo.weatherdex.data.city.implementation.remote.response.CityEntity
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface CityApi {
    @Headers("X-Api-Key: 1pDonunjt76TlAS4tPb4yg==JCWbLbLUhRGFzNJj")
    @GET("v1/city?name=")
    suspend fun fetchCity(
        @Query("name") name: String,
    ): Array<CityEntity>?
}
