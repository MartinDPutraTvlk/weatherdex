package com.julo.weatherdex.pages.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julo.weatherdex.base.network.http.HttpResponse
import com.julo.weatherdex.data.weather.api.model.WeatherData
import com.julo.weatherdex.data.weather.api.repository.WeatherRepository
import com.julo.weatherdex.pages.detail.DetailActivity.Companion.EXTRA_LATITUDE
import com.julo.weatherdex.pages.detail.DetailActivity.Companion.EXTRA_LONGITUDE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val latitude
        get() = savedStateHandle.get<Double>(EXTRA_LATITUDE) ?: 0.0

    private val longitude
        get() = savedStateHandle.get<Double>(EXTRA_LONGITUDE) ?: 0.0

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _weatherData = MutableStateFlow(WeatherData())
    val weatherData = _weatherData.asStateFlow()

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage = _errorMessage.asSharedFlow()

    init {
        fetchWeatherData()
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
                }
                is HttpResponse.Error -> {
                    _isLoading.update { false }
                    _errorMessage.emit(response.message)
                }
                else -> {
                    _isLoading.update { false }
                    _errorMessage.emit("Unknown error occurred")
                }
            }
        }
    }
}
