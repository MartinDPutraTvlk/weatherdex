package com.julo.weatherdex.weather.pages.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.julo.weatherdex.base.network.http.HttpResponse
import com.julo.weatherdex.core.extension.*
import com.julo.weatherdex.core.unit_test.CoroutineTestRule
import com.julo.weatherdex.data.city.api.model.City
import com.julo.weatherdex.data.city.api.repository.CityRepository
import com.julo.weatherdex.data.favorites.api.repository.FavoritesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class WeatherSearchCityViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    @get:Rule
    var mockkRule = MockKRule(this)

    private lateinit var viewModel: WeatherSearchCityViewModel

    @MockK
    lateinit var cityRepository: CityRepository

    @MockK
    lateinit var favoritesRepository: FavoritesRepository

    /** Test Cases **/
    @Test
    fun `test init block`() {
        /** Setup **/
        val result = List(3) {
            City()
        }
        coEvery { favoritesRepository.fetchFavoriteCities() } returns result

        /** Act **/
        viewModel = WeatherSearchCityViewModel(
            cityRepository = cityRepository,
            favoritesRepository = favoritesRepository,
        )

        /** Assert **/
        result assertEqual viewModel.favoriteCities.value
    }

    /**
     * Noteworthy Article: https://jeff-padgett.medium.com/unit-testing-mutablesharedflow-12aec5b6e05
     */
    @Test
    fun `test removeFavorite successful`() {
        /** Setup **/
        initDefault()

        val city = City(
            name = randomString()
        )
        val slotCity = slot<City>()
        coEvery { favoritesRepository.unfavoriteCity(capture(slotCity)) } returns true

        val favoriteCities = listOf<City>()
        coEvery { favoritesRepository.fetchFavoriteCities() } returns favoriteCities

        val emittedMessages = mutableListOf<String>()

        /** Act **/
        runTest(coroutineTestRule.testDispatcher) {
            val collectJob = launch {
                viewModel.snackbarMessage.collect {
                    emittedMessages.add(it)
                }
            }

            viewModel.removeFavorite(city)

            collectJob.cancel()
        }

        /** Assert **/
        emittedMessages.isNotEmpty().assertBoolean()
        city assertEqual slotCity.captured

        /**
         * 1st call is from the init block
         * 2nd call is from the block within the removeFavorite function
         */
        coVerify(exactly = 2) { favoritesRepository.fetchFavoriteCities() }
    }

    @Test
    fun `test removeFavorite failed`() = runTest {
        /** Setup **/
        initDefault()

        coEvery { favoritesRepository.unfavoriteCity(any()) } returns true

        val favoriteCities = listOf<City>()
        coEvery { favoritesRepository.fetchFavoriteCities() } returns favoriteCities

        val emittedMessages = mutableListOf<String>()

        /** Act **/
        runTest(coroutineTestRule.testDispatcher) {
            val collectJob = launch {
                viewModel.snackbarMessage.collect {
                    emittedMessages.add(it)
                }
            }

            viewModel.removeFavorite(City())

            collectJob.cancel()
        }

        /** Assert **/
        emittedMessages.isNotEmpty().assertBoolean()

        coVerify(exactly = 2) { favoritesRepository.fetchFavoriteCities() }
    }

    @Test
    fun `test searchCity if string is empty`() {
        /** Setup **/
        initDefault()

        /** Act **/
        viewModel.onSearchTextChange("")

        /** Assert **/
        viewModel.isSearching.value.assertBoolean(false)
        viewModel.cityData.value.assertListIsEmpty()
    }

    @Test
    fun `test searchCity, success response`() = runTest {
        /** Setup **/
        initDefault()

        val query = randomString()
        val slotQuery = slot<String>()
        val response = HttpResponse.Success(
            List(3) {
                City(
                    name = randomString(),
                    latitude = randomDouble(),
                    longitude = randomDouble(),
                )
            }
        )
        coEvery { cityRepository.fetchCityData(capture(slotQuery)) } returns response


        /** Act **/
        viewModel.onSearchTextChange(query)
        advanceTimeBy(2000)

        /** Assert **/
        query assertEqual slotQuery.captured
        query assertEqual viewModel.searchQuery.value
        viewModel.isSearching.value.assertBoolean(false)
        response.data assertListEqualToIgnoreOrder viewModel.cityData.value
    }

    @Test
    fun `test searchCity, error response`() = runTest(coroutineTestRule.testDispatcher) {
        /** Setup **/
        initDefault()

        val response = HttpResponse.Error(
            message = randomString()
        )
        coEvery { cityRepository.fetchCityData(any()) } returns response

        val emittedMessages = mutableListOf<String>()

        val collectJob = launch {
            viewModel.snackbarMessage.collect {
                emittedMessages.add(it)
            }
        }

        /** Act **/
        viewModel.onSearchTextChange(randomString())
        advanceTimeBy(2000)

        collectJob.cancel()


        /** Assert **/
        viewModel.isSearching.value.assertBoolean(false)
        viewModel.cityData.value.assertListIsEmpty()

        (response.message in emittedMessages).assertBoolean()
    }

    @Test
    fun `test searchCity, empty response`() = runTest(coroutineTestRule.testDispatcher) {
        /** Setup **/
        initDefault()

        val response = HttpResponse.Empty
        coEvery { cityRepository.fetchCityData(any()) } returns response

        val emittedMessages = mutableListOf<String>()

        val collectJob = launch {
            viewModel.snackbarMessage.collect {
                emittedMessages.add(it)
            }
        }

        /** Act **/
        viewModel.onSearchTextChange(randomString())
        advanceTimeBy(2000)

        collectJob.cancel()

        /** Assert **/
        viewModel.isSearching.value.assertBoolean(false)
        viewModel.cityData.value.assertListIsEmpty()

        ("City not found" in emittedMessages).assertBoolean()
    }

    @Test
    fun `test searchCity, unknown error`() = runTest(coroutineTestRule.testDispatcher) {
        /** Setup **/
        initDefault()

        val response = HttpResponse.Unknown
        coEvery { cityRepository.fetchCityData(any()) } returns response

        val emittedMessages = mutableListOf<String>()

        val collectJob = launch {
            viewModel.snackbarMessage.collect {
                emittedMessages.add(it)
            }
        }

        /** Act **/
        viewModel.onSearchTextChange(randomString())
        advanceTimeBy(2000)

        collectJob.cancel()

        /** Assert **/
        viewModel.isSearching.value.assertBoolean(false)
        viewModel.cityData.value.assertListIsEmpty()

        ("Unknown error occurred" in emittedMessages).assertBoolean()
    }

    private fun initDefault() {
        coEvery { favoritesRepository.fetchFavoriteCities() } returns listOf()
        viewModel = WeatherSearchCityViewModel(
            cityRepository = cityRepository,
            favoritesRepository = favoritesRepository,
        )
    }
}
