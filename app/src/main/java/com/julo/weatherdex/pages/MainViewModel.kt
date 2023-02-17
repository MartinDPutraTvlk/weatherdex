package com.julo.weatherdex.pages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julo.weatherdex.base.network.http.HttpResponse
import com.julo.weatherdex.data.city.api.model.City
import com.julo.weatherdex.data.city.api.repository.CityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val cityRepository: CityRepository,
): ViewModel() {

    /** PROPERTIES **/
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage = _errorMessage.asSharedFlow()

    private val _cityData = MutableStateFlow(City())
    val cityData = _cityData.asStateFlow()

    private var currentlyRunningJob: Job? = null

    /** PUBLIC FUN **/
    fun onSearchTextChange(query: String) {
        _searchQuery.value = query
        searchCity(query)
    }

    /** PRIVATE FUN **/
    private fun searchCity(query: String) {
        _isSearching.update { true }
        currentlyRunningJob?.cancel()
        currentlyRunningJob = viewModelScope.launch {
            delay(1000L)
            when(val response = cityRepository.fetchCityData(query)) {
                is HttpResponse.Success -> {
                    _cityData.update { response.data }
                    getWeatherForCity(city = response.data)
                }
                is HttpResponse.Error -> {
                    _cityData.update { City.EMPTY }
                    _isSearching.update { false }
                    _errorMessage.emit(response.message)
                }
                is HttpResponse.Empty -> {
                    _cityData.update { City.EMPTY }
                    _isSearching.update { false }
                    _errorMessage.emit("City not found")
                }
                else -> {
                    _cityData.update { City.EMPTY }
                    _isSearching.update { false }
                    _errorMessage.emit("Unknown error occurred")
                }
            }
        }
    }

    private fun getWeatherForCity(city: City) {

    }
}
