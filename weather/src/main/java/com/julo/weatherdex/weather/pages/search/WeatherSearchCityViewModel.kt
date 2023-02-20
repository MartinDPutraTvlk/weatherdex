package com.julo.weatherdex.weather.pages.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julo.weatherdex.base.network.http.HttpResponse
import com.julo.weatherdex.data.city.api.model.City
import com.julo.weatherdex.data.city.api.repository.CityRepository
import com.julo.weatherdex.data.favorites.api.repository.FavoritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherSearchCityViewModel @Inject constructor(
    private val cityRepository: CityRepository,
    private val favoritesRepository: FavoritesRepository,
): ViewModel() {

    /** PROPERTIES **/
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    private val _cityData = MutableStateFlow(listOf<City>())
    val cityData = _cityData.asStateFlow()

    private val _favoriteCities = MutableStateFlow(listOf<City>())
    val favoriteCities = _favoriteCities.asStateFlow()

    private var currentlyRunningJob: Job? = null

    init {
        fetchFavoriteCityData()
    }

    /** PUBLIC FUN **/
    fun onSearchTextChange(query: String) {
        _searchQuery.value = query
        searchCity(query)
    }

    fun fetchFavoriteCityData() {
        viewModelScope.launch {
            _favoriteCities.update {
                favoritesRepository.fetchFavoriteCities()
            }
        }
    }

    fun removeFavorite(city: City) {
        viewModelScope.launch {
            val success = favoritesRepository.unfavoriteCity(city)
            if(success) {
                _snackbarMessage.emit("Successfully removed ${city.name} from favorite list.")
            } else {
                _snackbarMessage.emit("Something went wrong. Please try again.")
            }
            fetchFavoriteCityData()
        }
    }

    /** PRIVATE FUN **/
    private fun searchCity(query: String) {
        if(query.isNotEmpty()) {
            _isSearching.update { true }
            currentlyRunningJob?.cancel()
            currentlyRunningJob = viewModelScope.launch {
                delay(1000L)
                when(val response = cityRepository.fetchCityData(query)) {
                    is HttpResponse.Success -> {
                        _cityData.update { response.data }
                        _isSearching.update { false }
                    }
                    is HttpResponse.Error -> {
                        _cityData.update { listOf() }
                        _isSearching.update { false }
                        _snackbarMessage.emit(response.message)
                    }
                    is HttpResponse.Empty -> {
                        _cityData.update { listOf() }
                        _isSearching.update { false }
                        _snackbarMessage.emit("City not found")
                    }
                    else -> {
                        _cityData.update { listOf() }
                        _isSearching.update { false }
                        _snackbarMessage.emit("Unknown error occurred")
                    }
                }
            }
        } else {
            _isSearching.update { false }
            currentlyRunningJob?.cancel()
            _cityData.update { listOf() }
        }
    }
}
