package com.julo.weatherdex.weather.pages.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julo.weatherdex.base.network.http.HttpResponse
import com.julo.weatherdex.data.city.api.model.City
import com.julo.weatherdex.data.favorites.api.repository.FavoritesRepository
import com.julo.weatherdex.data.weather.api.model.WeatherData
import com.julo.weatherdex.data.weather.api.repository.WeatherRepository
import com.julo.weatherdex.weather.pages.detail.WeatherDetailActivity.Companion.EXTRA_CITY_NAME
import com.julo.weatherdex.weather.pages.detail.WeatherDetailActivity.Companion.EXTRA_LATITUDE
import com.julo.weatherdex.weather.pages.detail.WeatherDetailActivity.Companion.EXTRA_LONGITUDE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherDetailViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val savedStateHandle: SavedStateHandle,
    private val favoritesRepository: FavoritesRepository,
): ViewModel() {
    private val latitude
        get() = savedStateHandle.get<Double>(EXTRA_LATITUDE) ?: 0.0

    private val longitude
        get() = savedStateHandle.get<Double>(EXTRA_LONGITUDE) ?: 0.0

    private val cityName
        get() = savedStateHandle.get<String>(EXTRA_CITY_NAME) ?: ""

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private val _weatherData = MutableStateFlow(WeatherData())
    val weatherData = _weatherData.asStateFlow()

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage = _errorMessage.asSharedFlow()

    private val _isCityFavorited = MutableStateFlow(false)
    val isCityFavorited = _isCityFavorited.asStateFlow()

    init {
        init()
    }

    fun onFavoriteButtonPressed() {
        if(isCityFavorited.value) {
            unfavoriteCity()
        } else {
            favoriteCity()
        }
    }

    fun init(isRefreshing: Boolean = false) {
        _isRefreshing.update { isRefreshing }
        fetchWeatherData()
        fetchFavoriteCity()
    }

    /** PRIVATE FUNS **/

    private fun fetchFavoriteCity() {
        viewModelScope.launch {
            val currentCity = City(
                name = cityName,
                latitude = latitude,
                longitude = longitude,
            )
            _isCityFavorited.update {
                favoritesRepository.isCityFavorited(currentCity)
            }
        }
    }

    private fun unfavoriteCity() {
        viewModelScope.launch {
            val currentCity = City(
                name = cityName,
                latitude = latitude,
                longitude = longitude,
            )
            val isSuccess = favoritesRepository.unfavoriteCity(currentCity)
            if(isSuccess) {
                _isCityFavorited.update { false }
            } else {
                _errorMessage.emit("Failed to unfavorite city")
            }
        }
    }
    private fun favoriteCity() {
        viewModelScope.launch {
            val currentCity = City(
                name = cityName,
                latitude = latitude,
                longitude = longitude,
            )
            val isSuccess = favoritesRepository.favoriteCity(currentCity)
            if(isSuccess) {
                _isCityFavorited.update { true }
            } else {
                _errorMessage.emit("Failed to favorite city")
            }
        }
    }

    private fun fetchWeatherData() {
        _isLoading.update { true }
        viewModelScope.launch {
            when(val response = weatherRepository.fetchWeatherData(
                lat = latitude,
                long = longitude,
            )) {
                is HttpResponse.Success -> {
                    _weatherData.update { response.data }
                    _isLoading.update { false }
                    _isRefreshing.update { false }
                }
                is HttpResponse.Error -> {
                    _isLoading.update { false }
                    _errorMessage.emit(response.message)
                    _isRefreshing.update { false }
                }
                else -> {
                    _isLoading.update { false }
                    _errorMessage.emit("Unknown error occurred")
                    _isRefreshing.update { false }
                }
            }
        }
    }
}
