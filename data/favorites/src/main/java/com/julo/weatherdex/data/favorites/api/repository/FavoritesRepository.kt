package com.julo.weatherdex.data.favorites.api.repository

import com.julo.weatherdex.data.city.api.model.City

interface FavoritesRepository {
    /**
     * Add a city to user's favorite list
     * Returns a [Boolean] indicating process result
     * */
    suspend fun favoriteCity(city: City): Boolean

    /**
     * Remove a city from user's favorite list
     * Returns a [Boolean] indicating process result
     * */
    suspend fun unfavoriteCity(city: City): Boolean

    /**
     * Check whether a city is user's favorite or not
     * */
    suspend fun isCityFavorited(city: City): Boolean

    /**
     * Fetch all cities that was favorited by the user
     * returns [List] of [City]
     * */
    suspend fun fetchFavoriteCities(): List<City>
}
