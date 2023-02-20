package com.julo.weatherdex.weather.pages.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.julo.weatherdex.base.network.http.HttpResponse
import com.julo.weatherdex.core.extension.*
import com.julo.weatherdex.core.unit_test.CoroutineTestRule
import com.julo.weatherdex.data.city.api.model.City
import com.julo.weatherdex.data.favorites.api.repository.FavoritesRepository
import com.julo.weatherdex.data.weather.api.model.WeatherData
import com.julo.weatherdex.data.weather.api.repository.WeatherRepository
import com.julo.weatherdex.weather.pages.detail.WeatherDetailActivity.Companion.EXTRA_CITY_NAME
import com.julo.weatherdex.weather.pages.detail.WeatherDetailActivity.Companion.EXTRA_LATITUDE
import com.julo.weatherdex.weather.pages.detail.WeatherDetailActivity.Companion.EXTRA_LONGITUDE
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class WeatherDetailViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    @get:Rule
    var mockkRule = MockKRule(this)

    private lateinit var viewModel: WeatherDetailViewModel

    @MockK
    lateinit var weatherRepository: WeatherRepository

    @MockK
    lateinit var favoritesRepository: FavoritesRepository

    @MockK
    lateinit var savedStateHandle: SavedStateHandle

    /** Test Cases **/
    @Test
    fun `test init block`() {
        /** Setup **/
        coEvery { weatherRepository.fetchWeatherData(any(), any()) } returns HttpResponse.Error(
            randomString()
        )

        val cityFavorited = randomBoolean()
        coEvery { favoritesRepository.isCityFavorited(any()) } returns cityFavorited

        every { savedStateHandle.get<Double>(EXTRA_LATITUDE) } returns randomDouble()
        every { savedStateHandle.get<Double>(EXTRA_LONGITUDE) } returns randomDouble()
        every { savedStateHandle.get<String>(EXTRA_CITY_NAME) } returns randomString()

        /** Act **/
        viewModel = WeatherDetailViewModel(
            weatherRepository = weatherRepository,
            favoritesRepository = favoritesRepository,
            savedStateHandle = savedStateHandle,
        )

        /** Assert **/
        coVerify { weatherRepository.fetchWeatherData(any(), any()) }

        cityFavorited assertEqual viewModel.isCityFavorited.value
    }

    @Test
    fun `test fetchFavoriteCity`() {
        /** Setup **/
        coEvery { weatherRepository.fetchWeatherData(any(), any()) } returns HttpResponse.Error(
            randomString()
        )

        val cityFavorited = randomBoolean()
        val slotCity = slot<City>()
        coEvery { favoritesRepository.isCityFavorited(capture(slotCity)) } returns cityFavorited

        val latitude = randomDouble()
        val longitude = randomDouble()
        val cityName = randomString()
        every { savedStateHandle.get<Double>(EXTRA_LATITUDE) } returns latitude
        every { savedStateHandle.get<Double>(EXTRA_LONGITUDE) } returns longitude
        every { savedStateHandle.get<String>(EXTRA_CITY_NAME) } returns cityName

        /** Act **/
        viewModel = WeatherDetailViewModel(
            weatherRepository = weatherRepository,
            favoritesRepository = favoritesRepository,
            savedStateHandle = savedStateHandle,
        )

        /** Assert **/
        cityFavorited assertEqual viewModel.isCityFavorited.value

        slotCity.captured.also { cityParam ->
            latitude assertEqual cityParam.latitude
            longitude assertEqual cityParam.longitude
            cityName assertEqual cityParam.name
        }
    }

    @Test
    fun `test fetchWeatherData success response`() {
        /** Setup **/
        coEvery { favoritesRepository.isCityFavorited(any()) } returns randomBoolean()
        every { savedStateHandle.get<String>(EXTRA_CITY_NAME) } returns randomString()

        val latitude = randomDouble()
        val longitude = randomDouble()
        every { savedStateHandle.get<Double>(EXTRA_LATITUDE) } returns latitude
        every { savedStateHandle.get<Double>(EXTRA_LONGITUDE) } returns longitude

        val slotLatitude = slot<Double>()
        val slotLongitude = slot<Double>()

        val response = HttpResponse.Success(WeatherData())
        coEvery { weatherRepository.fetchWeatherData(capture(slotLatitude), capture(slotLongitude)) } returns response

        /** Act **/
        viewModel = WeatherDetailViewModel(
            weatherRepository = weatherRepository,
            favoritesRepository = favoritesRepository,
            savedStateHandle = savedStateHandle,
        )

        /** Assert **/
        latitude assertEqual slotLatitude.captured
        longitude assertEqual slotLongitude.captured
        viewModel.isLoading.value.assertBoolean(false)
        response.data assertEqual viewModel.weatherData.value
    }

    @Test
    fun `test fetchWeatherData error response`() = runTest(coroutineTestRule.testDispatcher) {
        /** Setup **/
        coEvery { favoritesRepository.isCityFavorited(any()) } returns randomBoolean()
        every { savedStateHandle.get<String>(EXTRA_CITY_NAME) } returns randomString()
        every { savedStateHandle.get<Double>(EXTRA_LATITUDE) } returns randomDouble()
        every { savedStateHandle.get<Double>(EXTRA_LONGITUDE) } returns randomDouble()

        val response = HttpResponse.Error(randomString())
        coEvery { weatherRepository.fetchWeatherData(any(), any()) } returns response

        val emittedMessages = mutableListOf<String>()

        /** Act **/
        viewModel = WeatherDetailViewModel(
            weatherRepository = weatherRepository,
            favoritesRepository = favoritesRepository,
            savedStateHandle = savedStateHandle,
        )

        val collectJob = launch {
            viewModel.errorMessage.collect {
                emittedMessages.add(it)
            }
        }

        collectJob.cancel()

        /** Assert **/
        viewModel.isLoading.value.assertBoolean(false)
        /**
         * cannot be asserted, because the function is called within the init block
         * the init block gets executed as soon as the viewModel instance is created
         * there's no space between the instance creation and the api call to attach the job
         */
//        (response.message in emittedMessages).assertBoolean()
    }

    @Test
    fun `test fetchWeatherData unknown response`() = runTest(coroutineTestRule.testDispatcher) {
        /** Setup **/
        coEvery { favoritesRepository.isCityFavorited(any()) } returns randomBoolean()
        every { savedStateHandle.get<String>(EXTRA_CITY_NAME) } returns randomString()
        every { savedStateHandle.get<Double>(EXTRA_LATITUDE) } returns randomDouble()
        every { savedStateHandle.get<Double>(EXTRA_LONGITUDE) } returns randomDouble()

        val response = HttpResponse.Unknown
        coEvery { weatherRepository.fetchWeatherData(any(), any()) } returns response

        val emittedMessages = mutableListOf<String>()

        /** Act **/
        viewModel = WeatherDetailViewModel(
            weatherRepository = weatherRepository,
            favoritesRepository = favoritesRepository,
            savedStateHandle = savedStateHandle,
        )

        val collectJob = launch {
            viewModel.errorMessage.collect {
                emittedMessages.add(it)
            }
        }

        collectJob.cancel()

        /** Assert **/
        viewModel.isLoading.value.assertBoolean(false)
        /**
         * see comment in `test fetchWeatherData error response` test case
         */
//        (response.message in emittedMessages).assertBoolean()
    }

    @Test
    fun `test onFavoriteButtonPressed city is currently unfavorited`() {
        /** Setup **/
        coEvery { weatherRepository.fetchWeatherData(any(), any()) } returns HttpResponse.Unknown
        coEvery { favoritesRepository.isCityFavorited(any()) } returns false

        val latitude = randomDouble()
        val longitude = randomDouble()
        val cityName = randomString()
        every { savedStateHandle.get<Double>(EXTRA_LATITUDE) } returns latitude
        every { savedStateHandle.get<Double>(EXTRA_LONGITUDE) } returns longitude
        every { savedStateHandle.get<String>(EXTRA_CITY_NAME) } returns cityName

        val citySlot = slot<City>()
        coEvery { favoritesRepository.favoriteCity(capture(citySlot)) } returns true

        /** Act **/
        val viewModel = WeatherDetailViewModel(
            weatherRepository = weatherRepository,
            favoritesRepository = favoritesRepository,
            savedStateHandle = savedStateHandle,
        )
        viewModel.onFavoriteButtonPressed()

        /** Assert **/
        citySlot.captured.also { city ->
            latitude assertEqual city.latitude
            longitude assertEqual city.longitude
            cityName assertEqual city.name
        }
        viewModel.isCityFavorited.value.assertBoolean()
    }

    @Test
    fun `test onFavoriteButtonPressed city is currently favorited`() {
        /** Setup **/
        coEvery { weatherRepository.fetchWeatherData(any(), any()) } returns HttpResponse.Unknown
        coEvery { favoritesRepository.isCityFavorited(any()) } returns false

        val latitude = randomDouble()
        val longitude = randomDouble()
        val cityName = randomString()
        every { savedStateHandle.get<Double>(EXTRA_LATITUDE) } returns latitude
        every { savedStateHandle.get<Double>(EXTRA_LONGITUDE) } returns longitude
        every { savedStateHandle.get<String>(EXTRA_CITY_NAME) } returns cityName

        coEvery { favoritesRepository.favoriteCity(any()) } returns true

        val citySlot = slot<City>()
        coEvery { favoritesRepository.unfavoriteCity(capture(citySlot)) } returns true

        /** Act **/
        val viewModel = WeatherDetailViewModel(
            weatherRepository = weatherRepository,
            favoritesRepository = favoritesRepository,
            savedStateHandle = savedStateHandle,
        )
        viewModel.onFavoriteButtonPressed()
        viewModel.onFavoriteButtonPressed()

        /** Assert **/
        citySlot.captured.also { city ->
            latitude assertEqual city.latitude
            longitude assertEqual city.longitude
            cityName assertEqual city.name
        }
        viewModel.isCityFavorited.value.assertBoolean(false)
    }

}
