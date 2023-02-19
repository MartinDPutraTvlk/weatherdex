package com.julo.weatherdex.data.weather.implementation.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.julo.weatherdex.base.network.http.HttpResponse
import com.julo.weatherdex.core.extension.assertBoolean
import com.julo.weatherdex.core.extension.assertEqual
import com.julo.weatherdex.core.extension.randomDouble
import com.julo.weatherdex.core.extension.randomString
import com.julo.weatherdex.core.unit_test.CoroutineTestRule
import com.julo.weatherdex.data.weather.implementation.remote.api.WeatherApi
import com.julo.weatherdex.data.weather.implementation.remote.response.WeatherEntity
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class WeatherRepositoryImplTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    @get:Rule
    var mockkRule = MockKRule(this)

    private lateinit var repository: WeatherRepositoryImpl

    @MockK
    lateinit var weatherApi: WeatherApi

    @Before
    fun setup() {
        repository = WeatherRepositoryImpl(
            weatherApi = weatherApi,
            ioDispatcher = coroutineTestRule.testDispatcher,
        )
    }

    /** Test Cases **/
    @Test
    fun `test fetchWeatherData null response`() = runTest {
        /** Setup **/
        coEvery { weatherApi.fetchDailyWeather(any(), any()) } returns null

        /** Act **/
        val result = repository.fetchWeatherData(randomDouble(), randomDouble())

        /** Assert **/
        HttpResponse.Empty assertEqual result
    }

    @Test
    fun `test fetchWeatherData exception response`() = runTest {
        /** Setup **/
        val exceptionMessage = randomString()
        coEvery { weatherApi.fetchDailyWeather(any(), any()) } throws IllegalStateException(exceptionMessage)

        /** Act **/
        val result = repository.fetchWeatherData(randomDouble(), randomDouble())

        /** Assert **/
        exceptionMessage assertEqual (result as HttpResponse.Error).message
    }

    @Test
    fun `test fetchWeatherData success response`() = runTest {
        /** Setup **/
        val slotLatitude = slot<Double>()
        val slotLongitude = slot<Double>()

        val response = WeatherEntity(
            current = null,
            daily = null,
            lat = null,
            lon = null,
            timezone = null,
            timezone_offset = null,
        )
        coEvery { weatherApi.fetchDailyWeather(
            capture(slotLatitude),
            capture(slotLongitude),
        ) } returns response

        val latitude = randomDouble()
        val longitude = randomDouble()

        /** Act **/
        val result = repository.fetchWeatherData(latitude, longitude)

        /** Assert **/
        (result is HttpResponse.Success).assertBoolean()

        latitude assertEqual slotLatitude.captured
        longitude assertEqual slotLongitude.captured
    }
}
