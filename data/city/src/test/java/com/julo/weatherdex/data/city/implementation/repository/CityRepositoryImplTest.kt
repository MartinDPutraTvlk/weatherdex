package com.julo.weatherdex.data.city.implementation.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.julo.weatherdex.base.network.http.HttpResponse
import com.julo.weatherdex.core.extension.*
import com.julo.weatherdex.core.unit_test.CoroutineTestRule
import com.julo.weatherdex.data.city.implementation.remote.api.CityApi
import com.julo.weatherdex.data.city.implementation.remote.response.CityEntity
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
class CityRepositoryImplTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    @get:Rule
    var mockkRule = MockKRule(this)

    private lateinit var repository: CityRepositoryImpl

    @MockK
    lateinit var cityApi: CityApi

    @Before
    fun setup() {
        repository = CityRepositoryImpl(
            cityApi = cityApi,
            ioDispatcher = coroutineTestRule.testDispatcher,
        )
    }

    /** Test Cases **/
    @Test
    fun `test fetchWeatherData null response`() = runTest {
        /** Setup **/
        coEvery { cityApi.fetchCity(any()) } returns null

        /** Act **/
        val result = repository.fetchCityData(randomString())

        /** Assert **/
        HttpResponse.Empty assertEqual result
    }

    @Test
    fun `test fetchWeatherData empty response`() = runTest {
        /** Setup **/
        coEvery { cityApi.fetchCity(any()) } returns emptyArray()

        /** Act **/
        val result = repository.fetchCityData(randomString())

        /** Assert **/
        HttpResponse.Empty assertEqual result
    }

    @Test
    fun `test fetchWeatherData throws exception`() = runTest {
        /** Setup **/
        val exceptionMessage = randomString()
        coEvery { cityApi.fetchCity(any()) } throws IllegalStateException(exceptionMessage)

        /** Act **/
        val result = repository.fetchCityData(randomString())

        /** Assert **/
        exceptionMessage assertEqual (result as HttpResponse.Error).message
    }


    @Test
    fun `test fetchWeatherData success response`() = runTest {
        /** Setup **/
        val cityName = randomString()

        val resultData = arrayOf(
            CityEntity(
                name = randomString(),
                latitude = randomDouble(),
                longitude = randomDouble(),
                country = randomString(),
                population = randomInt(),
                isCapital = randomBoolean(),
            )
        )

        val slotCityName = slot<String>()
        val slotResultLimit = slot<Int>()
        coEvery { cityApi.fetchCity(
            capture(slotCityName),
            capture(slotResultLimit),
        ) } returns resultData

        /** Act **/
        val result = repository.fetchCityData(cityName)

        /** Assert **/
        (result is HttpResponse.Success).assertBoolean()

        cityName assertEqual slotCityName.captured
        5 assertEqual slotResultLimit.captured
    }
}
