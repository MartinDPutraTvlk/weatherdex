package com.julo.weatherdex.pages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julo.weatherdex.base.network.http.HttpResponse
import com.julo.weatherdex.data.city.api.model.City
import com.julo.weatherdex.data.city.api.repository.CityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val cityRepository: CityRepository,
): ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

    private val _cityData = MutableStateFlow(City())
    val cityData = _cityData.asStateFlow()

    fun onSearchTextChange(query: String) {
        _searchQuery.value = query
        searchCity(query)
    }

    private fun searchCity(query: String) {
        viewModelScope.launch {
            when(val response = cityRepository.fetchCityData(query)) {
                is HttpResponse.Success -> {
                    _cityData.update { response.data }
                    getWeatherForCity(city = response.data)
                }
                is HttpResponse.Error -> {
                    _errorMessage.update {
                        response.message
                    }
                }
                is HttpResponse.Empty -> {
                    _errorMessage.update {
                        "City not found"
                    }
                }
                else -> Unit
            }
        }
    }

    private fun getWeatherForCity(city: City) {

    }
}
