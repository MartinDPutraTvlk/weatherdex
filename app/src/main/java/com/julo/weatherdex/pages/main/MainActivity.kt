package com.julo.weatherdex.pages.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.julo.weatherdex.R
import com.julo.weatherdex.data.city.api.model.City
import com.julo.weatherdex.pages.detail.DetailActivity
import com.julo.weatherdex.ui.theme.BrightRed
import com.julo.weatherdex.ui.theme.FontFace
import com.julo.weatherdex.ui.theme.LightPink
import com.julo.weatherdex.ui.theme.WeatherdexTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = viewModel<MainViewModel>()
            val searchQuery by viewModel.searchQuery.collectAsState()
            val isSearching by viewModel.isSearching.collectAsState()
            val cityData by viewModel.cityData.collectAsState()

            val scaffoldState = rememberScaffoldState()

            LaunchedEffect(Unit) {
                viewModel.errorMessage.collectLatest {
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
                            backgroundColor = LightPink,
                            contentColor = BrightRed,
                            actionColor = Color.Black,
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
                            Box(modifier = Modifier.fillMaxSize()) {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        } else {
                            if (cityData.isNotEmpty) {
                                CityCard(city = cityData)
                            }
                        }
                    }
                }
            )
        }
    }

    @Composable
    fun CityCard(city: City) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    navigateToDetailPage(city)
                },
            shape = RoundedCornerShape(16.dp),
            elevation = 6.dp,
        ) {
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

                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = stringResource(R.string.julo_text_population, city.population),
                    style = FontFace.Small.normal
                )
            }
        }

    }

    private fun navigateToDetailPage(city: City) {
        DetailActivity.startActivity(
            this,
            latitude = city.latitude,
            longitude = city.longitude,
            cityName = city.name
        )
    }
}
