package com.julo.weatherdex.pages.detail

import android.app.Activity
import android.app.LocaleConfig
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.julo.weatherdex.R
import com.julo.weatherdex.core.extension.notNull
import com.julo.weatherdex.data.weather.api.model.WeatherData
import com.julo.weatherdex.data.weather.api.model.child.ForecastWeather
import com.julo.weatherdex.data.weather.api.model.child.TodayWeather
import com.julo.weatherdex.data.weather.api.model.constant.DailyTemperatureTime
import com.julo.weatherdex.ui.theme.FontFace
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*


@AndroidEntryPoint
class DetailActivity : ComponentActivity() {
    private lateinit var viewModel: DetailViewModel

    private val dateFormat = SimpleDateFormat("EEEE, dd MMM yyyy", Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            viewModel = viewModel()
            val isLoading by viewModel.isLoading.collectAsState()
            val weatherData by viewModel.weatherData.collectAsState()
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            } else {
                PageContent(weatherData)
            }
        }
    }

    @Composable
    private fun PageContent(weatherData: WeatherData) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 16.dp,
                    start = 16.dp,
                    end = 16.dp
                )
        ) {
            Header(weatherData.today)
            Body(
                modifier = Modifier
                    .padding(top = 16.dp),
                daily = weatherData.daily,
            )
        }
    }

    @Composable
    private fun Body(
        modifier: Modifier = Modifier,
        daily: List<ForecastWeather>
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.julo_text_weather_forecast),
                style = FontFace.Huge.bold
            )
            LazyColumn(
                modifier = Modifier.padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(daily) {
                    ForecastCard(it)
                }
            }
        }

    }

    @Composable
    private fun ForecastCard(forecastWeather: ForecastWeather) {
        Card(
            modifier = Modifier,
            shape = RoundedCornerShape(16.dp),
            elevation = 6.dp,
        ) {
            Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                ForecastCardWeatherPrediction(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .heightIn(max = 125.dp)
                        .fillMaxWidth(0.2f),
                    forecastWeather = forecastWeather,
                )
                Divider(
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                )
                ForecastCardWeatherInformation(
                    modifier = Modifier
                        .padding(8.dp),
                    forecastWeather = forecastWeather,
                )
            }
        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    private fun ForecastCardWeatherPrediction(
        modifier: Modifier = Modifier,
        forecastWeather: ForecastWeather
    ) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GlideImage(
                model = forecastWeather.weather.iconUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
            )
            Divider(
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
            )
            Text(
                text = forecastWeather.weather.title,
                style = FontFace.Big.normal,
                modifier = Modifier.padding(
                    top = 4.dp,
                    start = 4.dp,
                    end = 4.dp
                )
            )
            Text(
                text = forecastWeather.weather.description,
                style = FontFace.Regular.secondary,
                modifier = Modifier
                    .padding(
                        top = 4.dp,
                        start = 4.dp,
                        end = 4.dp
                    ),
                textAlign = TextAlign.Center,
            )
        }
    }

    @Composable
    private fun ForecastCardWeatherInformation(
        modifier: Modifier = Modifier,
        forecastWeather: ForecastWeather
    ) {
        Column(modifier = modifier.fillMaxSize()) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = getDateFormat(forecastWeather.dateInMillis),
                style = FontFace.Big.normal
            )
            ImageWithText(
                modifier = Modifier.padding(top = 8.dp),
                resourceId = R.drawable.ic_humidity,
                text = stringResource(
                    id = R.string.julo_text_humidity,
                    forecastWeather.humidity.toString(),
                )
            )
            ImageWithText(
                modifier = Modifier.padding(top = 8.dp),
                resourceId = R.drawable.ic_wind,
                text = stringResource(
                    id = R.string.julo_text_wind_speed,
                    forecastWeather.windSpeed.toString(),
                )
            )
            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(id = R.drawable.ic_temperature),
                    contentDescription = null,
                )
                TemperatureInformation(
                    modifier = Modifier.padding(start = 8.dp),
                    morning = forecastWeather.temps[DailyTemperatureTime.morning].notNull(),
                    day = forecastWeather.temps[DailyTemperatureTime.day].notNull(),
                    evening = forecastWeather.temps[DailyTemperatureTime.evening].notNull(),
                    night = forecastWeather.temps[DailyTemperatureTime.night].notNull(),
                    title = stringResource(id = R.string.julo_text_actual_temperature),
                )
                Spacer(modifier = Modifier.width(40.dp))
                TemperatureInformation(
                    morning = forecastWeather.feelsLike[DailyTemperatureTime.morning].notNull(),
                    day = forecastWeather.feelsLike[DailyTemperatureTime.day].notNull(),
                    evening = forecastWeather.feelsLike[DailyTemperatureTime.evening].notNull(),
                    night = forecastWeather.feelsLike[DailyTemperatureTime.night].notNull(),
                    title = stringResource(id = R.string.julo_text_feels_like),
                )
            }
        }
    }

    @Composable
    private fun TemperatureInformation(
        modifier: Modifier = Modifier,
        morning: Double,
        day: Double,
        evening: Double,
        night: Double,
        title: String,
    ) {
        Column(modifier = modifier) {
            Text(
                text = title,
                style = FontFace.Regular.bold,
                textAlign = TextAlign.Center,
            )
            Text(
                text = stringResource(
                    id = R.string.julo_text_temperature_morning_celcius_degree,
                    morning
                ),
                style = FontFace.Regular.normal
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = stringResource(
                    id = R.string.julo_text_temperature_day_celcius_degree,
                    day
                ),
                style = FontFace.Regular.normal
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = stringResource(
                    id = R.string.julo_text_temperature_evening_celcius_degree,
                    evening
                ),
                style = FontFace.Regular.normal
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = stringResource(
                    id = R.string.julo_text_temperature_night_celcius_degree,
                    night
                ),
                style = FontFace.Regular.normal
            )
        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    private fun Header(today: TodayWeather) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = 6.dp,
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GlideImage(
                    model = today.weather.iconUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                )

                Text(
                    modifier = Modifier.padding(top = 16.dp),
                    text = getDateFormat(today.dateInMillis),
                    style = FontFace.Huge.normal
                )

                val cityName = intent.getStringExtra(EXTRA_CITY_NAME).notNull()
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = cityName,
                    style = FontFace.Huge.bold
                )

                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = stringResource(
                        id = R.string.julo_text_todays_weather,
                        today.weather.title,
                    ),
                    style = FontFace.Big.bold
                )
                Text(
                    text = today.weather.description,
                    style = FontFace.Regular.secondary
                )

                ImageWithText(
                    modifier = Modifier.padding(top = 8.dp),
                    resourceId = R.drawable.ic_humidity,
                    text = stringResource(
                        id = R.string.julo_text_humidity,
                        today.humidity.toString(),
                    )
                )

                ImageWithDoubleText(
                    modifier = Modifier.padding(top = 8.dp),
                    resourceId = R.drawable.ic_temperature,
                    firstText = stringResource(
                        id = R.string.julo_text_temperature_celcius_degree,
                        today.temp.toString(),
                    ),
                    secondText = stringResource(
                        id = R.string.julo_text_temperature_feels_like_celcius_degree,
                        today.tempFeelsLike.toString(),
                    ),
                )
            }
        }
    }

    @Composable
    private fun ImageWithText(
        modifier: Modifier = Modifier,
        resourceId: Int,
        text: String,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
        ) {
            Image(
                modifier = Modifier.size(16.dp),
                painter = painterResource(id = resourceId),
                contentDescription = null,
            )
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = text,
                style = FontFace.Regular.normal
            )
        }
    }

    @Composable
    private fun ImageWithDoubleText(
        modifier: Modifier = Modifier,
        resourceId: Int,
        firstText: String,
        secondText: String,
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.size(16.dp),
                painter = painterResource(id = resourceId),
                contentDescription = null,
            )
            Column(
                modifier = Modifier.padding(start = 4.dp),
            ) {
                Text(
                    text = firstText,
                    style = FontFace.Regular.normal
                )
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = secondText,
                    style = FontFace.Small.secondary
                )
            }
        }
    }

    private fun getDateFormat(millis: Long): String {
        return dateFormat.format(millis)
    }

    companion object {
        const val EXTRA_LATITUDE = "EXTRA_LAT"
        const val EXTRA_LONGITUDE = "EXTRA_LONG"
        const val EXTRA_CITY_NAME = "EXTRA_CITY_NAME"

        fun startActivity(
            activity: Activity,
            latitude: Double,
            longitude: Double,
            cityName: String,
        ) {
            activity.startActivity(
                Intent(activity, DetailActivity::class.java).apply {
                    putExtra(EXTRA_LATITUDE, latitude)
                    putExtra(EXTRA_LONGITUDE, longitude)
                    putExtra(EXTRA_CITY_NAME, cityName)
                }
            )
        }
    }
}
