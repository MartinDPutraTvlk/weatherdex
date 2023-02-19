package com.julo.weatherdex

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.julo.weatherdex.weather.pages.search.WeatherSearchCityActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppEntryActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigateToSearchCityActivity()
    }

    private fun navigateToSearchCityActivity() {
        WeatherSearchCityActivity.startActivity(this)
        finish()
    }
}
