package com.julo.weatherdex.data.city.implementation.mapper

import com.julo.weatherdex.core.extension.*
import com.julo.weatherdex.data.city.implementation.remote.response.CityEntity
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CityMapperTest {
    @Test
    fun `testCityMapper with null array`() {
        /** Setup **/
        val cityEntities: Array<CityEntity>? = null

        /** Act **/
        val result = cityEntities.toCities()

        /** Assert **/
        result.assertIsEmpty()
    }

    @Test
    fun `testCityMapper with non empty data`() {
        /** Setup **/
        val cityEntities = Array(3) {
            CityEntity(
                name = randomString(),
                latitude = randomDouble(),
                longitude = randomDouble(),
                country = randomString(),
                population = randomInt(),
                isCapital = randomBoolean(),
            )
        }

        /** Act **/
        val result = cityEntities.toCities()

        /** Assert **/
        result.forEachIndexed { index, viewParam ->
            cityEntities[index].also { cityEntity ->
                cityEntity.name assertEqual viewParam.name
                cityEntity.latitude assertEqual viewParam.latitude
                cityEntity.longitude assertEqual viewParam.longitude
                cityEntity.country assertEqual viewParam.country
                cityEntity.population assertEqual viewParam.population
                cityEntity.isCapital assertEqual viewParam.isCapital
            }
        }
    }
}
