@file:Suppress("UNCHECKED_CAST", "ApplySharedPref")

package com.julo.weatherdex.data.favorites.implementation.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import com.julo.weatherdex.core.extension.equalsIgnoreCase
import com.julo.weatherdex.data.city.api.model.City
import com.julo.weatherdex.data.favorites.api.repository.FavoritesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavoritesRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson,
    private val ioDispatcher: CoroutineDispatcher,
) : FavoritesRepository {
    companion object {
        private const val FAVORITE_CITIES_KEY = "FAVORITE_CITIES_KEY"
    }

    override suspend fun favoriteCity(city: City): Boolean {
        return withContext(context = ioDispatcher) {
            try {
                val editor = sharedPreferences.edit()
                val cityJson = gson.toJson(city)
                editor.putString(FAVORITE_CITIES_KEY, cityJson)
                editor.commit()
            } catch (e: Exception) {
                false
            }
        }
    }

    // TODO: Unused param. Implement for multiple favorite cities
    override suspend fun unfavoriteCity(city: City): Boolean {
        return withContext(context = ioDispatcher) {
            try {
                sharedPreferences.edit().remove(FAVORITE_CITIES_KEY).commit()
            } catch (e: Exception) {
                false
            }
        }
    }

    override suspend fun isCityFavorited(city: City): Boolean {
        return withContext(context = ioDispatcher) {
            try {
                val favoritedCityJson = sharedPreferences.getString(FAVORITE_CITIES_KEY, "")
                if(favoritedCityJson.isNullOrEmpty()) {
                    return@withContext false
                }
                val favoritedCity = gson.fromJson(favoritedCityJson, City::class.java)
                return@withContext favoritedCity.name equalsIgnoreCase city.name
            } catch (e: Exception) {
                false
            }
        }
    }

    override suspend fun fetchFavoriteCities(): List<City> {
        return withContext(context = ioDispatcher) {
            try {
                val favoritedCityJson = sharedPreferences.getString(FAVORITE_CITIES_KEY, "")
                if(favoritedCityJson.isNullOrEmpty()) {
                    return@withContext emptyList()
                }
                val favoritedCity = gson.fromJson(favoritedCityJson, City::class.java)
                return@withContext listOf(favoritedCity)
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
}
