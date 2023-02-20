package com.julo.weatherdex.data.weather.implementation.mapper

import com.julo.weatherdex.core.extension.notNull
import com.julo.weatherdex.data.weather.api.model.WeatherData
import com.julo.weatherdex.data.weather.api.model.child.ForecastWeather
import com.julo.weatherdex.data.weather.api.model.child.TodayWeather
import com.julo.weatherdex.data.weather.api.model.child.Weather
import com.julo.weatherdex.data.weather.api.model.constant.DailyTemperatureTime
import com.julo.weatherdex.data.weather.implementation.remote.response.WeatherEntity
import com.julo.weatherdex.data.weather.implementation.remote.response.child.*
import java.util.*

fun WeatherEntity.toWeatherData() = WeatherData(
    daily = daily.toForecastWeather(timezone_offset.notNull()),
    today = current.toToday(timezone_offset.notNull())
)

private fun List<DailyEntity>?.toForecastWeather(timezoneOffset: Int) = this?.mapIndexed { index, dailyEntity ->
    val todayMillis = getDateInMillis(timezoneOffset)
    val nextDayMillis = (index + 1) * millisInADay
    ForecastWeather(
        dateInMillis = todayMillis + nextDayMillis,
        humidity = dailyEntity.humidity.notNull(),
        windSpeed = dailyEntity.windSpeed.notNull(),
        temps = dailyEntity.temp.toTemps(),
        feelsLike = dailyEntity.feelsLike.toFeelsLike(),
        weather = dailyEntity.weather.toWeather(),
    )
}.notNull()

private fun FeelsLikeEntity?.toFeelsLike() = this?.let {
    mapOf(
        DailyTemperatureTime.morning to it.morning.notNull(),
        DailyTemperatureTime.day to it.day.notNull(),
        DailyTemperatureTime.evening to it.evening.notNull(),
        DailyTemperatureTime.night to it.night.notNull(),
    )
}.notNull()

private fun TempEntity?.toTemps() = this?.let {
    mapOf(
        DailyTemperatureTime.morning to it.morning.notNull(),
        DailyTemperatureTime.day to it.day.notNull(),
        DailyTemperatureTime.evening to it.evening.notNull(),
        DailyTemperatureTime.night to it.night.notNull(),
        DailyTemperatureTime.min to it.min.notNull(),
        DailyTemperatureTime.max to it.max.notNull(),
    )
}.notNull()

private fun CurrentEntity?.toToday(timezoneOffset: Int) = TodayWeather(
    dateInMillis = getDateInMillis(timezoneOffset),
    humidity = this?.humidity.notNull(),
    temp = this?.temp.notNull(),
    tempFeelsLike = this?.feelsLike.notNull(),
    weather = this?.weather.toWeather(),
)

private fun getDateInMillis(timezoneOffset: Int) = Calendar.getInstance().timeInMillis + timezoneOffset

private fun List<DailyWeatherEntity>?.toWeather() = this?.getOrNull(0)?.let {
    Weather(
        title = it.title.notNull(),
        description = it.description.notNull(),
        iconUrl = it.icon?.let { iconId ->
            "https://openweathermap.org/img/wn/$iconId@2x.png"
        }.notNull()
    )
} ?: Weather()

private const val millisInADay = 1000 * 60 * 60 * 24
