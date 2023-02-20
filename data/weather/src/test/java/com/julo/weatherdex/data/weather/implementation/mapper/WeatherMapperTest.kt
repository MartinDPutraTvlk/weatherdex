package com.julo.weatherdex.data.weather.implementation.mapper

import com.julo.weatherdex.core.extension.*
import com.julo.weatherdex.data.weather.api.model.constant.DailyTemperatureTime
import com.julo.weatherdex.data.weather.implementation.remote.response.WeatherEntity
import com.julo.weatherdex.data.weather.implementation.remote.response.child.*
import io.mockk.every
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*

@RunWith(JUnit4::class)
class WeatherMapperTest {
    @get:Rule
    var mockkRule = MockKRule(this)

    @Test
    fun `test toToday`() {
        /** Setup **/
        val entity = WeatherEntity(
            timezone_offset = randomInt(absolute = true),
            current = CurrentEntity(
                humidity = randomInt(),
                temp = randomDouble(),
                feelsLike = randomDouble(),
                weather = listOf(),
            )
        )

        val calendar = mockk<Calendar>()
        mockkStatic(Calendar::class)
        every { Calendar.getInstance() } returns calendar

        val currentMillis = randomLong()
        every { calendar.timeInMillis } returns currentMillis

        /** Act **/
        val result = entity.toWeatherData()

        /** Assert **/

        result.today.also { today ->
            entity.current!!.humidity assertEqual today.humidity
            entity.current!!.temp assertEqual today.temp
            entity.current!!.feelsLike assertEqual today.tempFeelsLike
            (entity.timezone_offset!! + currentMillis) assertEqual today.dateInMillis
        }
    }

    @Test
    fun `test toWeather`() {
        /** Setup **/
        val weatherEntity = DailyWeatherEntity(
            description = randomString(),
            title = randomString(),
            icon = randomString(3),
        )

        val entity = WeatherEntity(
            timezone_offset = randomInt(absolute = true),
            current = CurrentEntity(
                weather = listOf(weatherEntity),
            )
        )

        val calendar = mockk<Calendar>()
        mockkStatic(Calendar::class)
        every { Calendar.getInstance() } returns calendar
        every { calendar.timeInMillis } returns randomLong()

        /** Act **/
        val result = entity.toWeatherData()

        /** Assert **/
        result.today.weather.also { todayWeather ->
            weatherEntity.title assertEqual todayWeather.title
            weatherEntity.description assertEqual todayWeather.description
            "https://openweathermap.org/img/wn/${weatherEntity.icon}@2x.png" assertEqual todayWeather.iconUrl
        }
    }

    @Test
    fun `test toForecastWeather`() {
        /** Setup **/

        val entity = WeatherEntity(
            timezone_offset = randomInt(absolute = true),
            daily = List(3) {
                DailyEntity(
                    humidity = randomInt(),
                    windSpeed = randomDouble(),
                    weather = listOf(),
                    temp = TempEntity(
                        morning = randomDouble(),
                        day = randomDouble(),
                        evening = randomDouble(),
                        night = randomDouble(),
                        min = randomDouble(),
                        max = randomDouble(),
                    ),
                    feelsLike = FeelsLikeEntity(
                        morning = randomDouble(),
                        day = randomDouble(),
                        evening = randomDouble(),
                        night = randomDouble(),
                    ),
                )
            }
        )

        val calendar = mockk<Calendar>()
        mockkStatic(Calendar::class)
        every { Calendar.getInstance() } returns calendar

        val currentMillis = randomLong()
        every { calendar.timeInMillis } returns currentMillis

        /** Act **/
        val result = entity.toWeatherData()

        /** Assert **/
        val millisInADay = 1000 * 60 * 60 * 24

        result.daily.forEachIndexed { index, forecastViewParam ->
            val todayMillis = currentMillis + entity.timezone_offset!!
            val nextDayMillis = (index + 1) * millisInADay
            entity.daily!![index].also { dailyEntity ->
                (todayMillis + nextDayMillis) assertEqual forecastViewParam.dateInMillis

                dailyEntity.humidity assertEqual forecastViewParam.humidity
                dailyEntity.windSpeed assertEqual forecastViewParam.windSpeed
                dailyEntity.temp!!.also { tempEntity ->
                    tempEntity.morning assertEqual forecastViewParam.temps[DailyTemperatureTime.morning]
                    tempEntity.day assertEqual forecastViewParam.temps[DailyTemperatureTime.day]
                    tempEntity.evening assertEqual forecastViewParam.temps[DailyTemperatureTime.evening]
                    tempEntity.night assertEqual forecastViewParam.temps[DailyTemperatureTime.night]
                    tempEntity.min assertEqual forecastViewParam.temps[DailyTemperatureTime.min]
                    tempEntity.max assertEqual forecastViewParam.temps[DailyTemperatureTime.max]
                }
                dailyEntity.feelsLike!!.also { feelsLikeEntity ->
                    feelsLikeEntity.morning assertEqual forecastViewParam.feelsLike[DailyTemperatureTime.morning]
                    feelsLikeEntity.day assertEqual forecastViewParam.feelsLike[DailyTemperatureTime.day]
                    feelsLikeEntity.evening assertEqual forecastViewParam.feelsLike[DailyTemperatureTime.evening]
                    feelsLikeEntity.night assertEqual forecastViewParam.feelsLike[DailyTemperatureTime.night]
                }
            }
        }
    }
}
