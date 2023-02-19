package com.julo.weatherdex.weather.pages.search

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.julo.weatherdex.data.city.api.model.City
import com.julo.weatherdex.weather.R
import com.julo.weatherdex.weather.pages.detail.WeatherDetailActivity
import com.julo.weatherdex.weather.ui.theme.FontFace
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class WeatherSearchCityActivity : ComponentActivity() {
    private lateinit var viewModel: WeatherSearchCityViewModel
    override fun onResume() {
        super.onResume()
        if(this::viewModel.isInitialized) {
            viewModel.fetchFavoriteCityData()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            viewModel = viewModel()
            val searchQuery by viewModel.searchQuery.collectAsState()
            val isSearching by viewModel.isSearching.collectAsState()
            val cityData by viewModel.cityData.collectAsState()
            val favoritedCities by viewModel.favoriteCities.collectAsState()

            val scaffoldState = rememberScaffoldState()

            LaunchedEffect(Unit) {
                viewModel.snackbarMessage.collectLatest {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = it,
                        duration = SnackbarDuration.Short,
                        actionLabel = getString(R.string.julo_text_close),
                    )
                }
            }

            Scaffold(
                scaffoldState = scaffoldState,
                snackbarHost = {
                    SnackbarHost(it) { data ->
                        Snackbar(
                            snackbarData = data,
                        )
                    }
                },
                content = { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(16.dp)
                    ) {
                        TextField(
                            value = searchQuery,
                            onValueChange = viewModel::onSearchTextChange,
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            placeholder = {
                                Text(text = stringResource(id = R.string.julo_text_enter_city))
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        if (isSearching) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        } else {
                            if (cityData.isNotEmpty()) {
                                LazyColumn(
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    contentPadding = PaddingValues(vertical = 16.dp)
                                ) {
                                    items(cityData) { city ->
                                        CityCard(city = city)
                                    }
                                }
                            }
                        }
                        if (favoritedCities.isNotEmpty()) {
                            Text(
                                modifier = Modifier.padding(top = 16.dp),
                                text = stringResource(id = R.string.julo_text_favorite_cities),
                                style = FontFace.Big.bold,
                            )
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                contentPadding = PaddingValues(vertical = 16.dp)
                            ) {
                                items(favoritedCities) { favoritedCity ->
                                    CityCard(city = favoritedCity, isFavorited = true)
                                }
                            }
                        }

                    }
                }
            )
        }
    }

    @Composable
    fun CityCard(city: City, isFavorited: Boolean = false) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    navigateToDetailPage(city)
                },
            shape = RoundedCornerShape(16.dp),
            elevation = 6.dp,
        ) {
            Box {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = city.name,
                            style = FontFace.Big.bold
                        )
                        Text(
                            modifier = Modifier.padding(start = 4.dp),
                            text = city.country,
                            style = FontFace.Regular.secondary
                        )
                    }

                    if(city.population != 0) {
                        Text(
                            modifier = Modifier.padding(top = 4.dp),
                            text = stringResource(R.string.julo_text_population, city.population),
                            style = FontFace.Small.normal
                        )
                    }
                }
                if (isFavorited) {
                    Image(
                        modifier = Modifier
                            .size(32.dp)
                            .align(Alignment.TopEnd)
                            .padding(end = 8.dp, top = 8.dp)
                            .clickable { viewModel.removeFavorite(city) },
                        painter = painterResource(id = R.drawable.ic_heart_filled),
                        contentDescription = null,
                    )
                }
            }
        }

    }

    private fun navigateToDetailPage(city: City) {
        WeatherDetailActivity.startActivity(
            this,
            latitude = city.latitude,
            longitude = city.longitude,
            cityName = city.name
        )
    }

    companion object {
        fun startActivity(
            activity: Activity,
        ) {
            activity.startActivity(
                Intent(activity, WeatherSearchCityActivity::class.java)
            )
        }
    }
}
