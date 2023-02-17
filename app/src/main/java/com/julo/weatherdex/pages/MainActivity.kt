package com.julo.weatherdex.pages

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.julo.weatherdex.ui.theme.WeatherdexTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherdexTheme {
                val viewModel = viewModel<MainViewModel>()
                val searchQuery by viewModel.searchQuery.collectAsState()
                val isSearching by viewModel.isSearching.collectAsState()
                val cityData by viewModel.cityData.collectAsState()

                val scaffoldState = rememberScaffoldState()
                val errorMessage by viewModel.errorMessage.collectAsState("")
                LaunchedEffect(errorMessage) {
                    if(errorMessage.isNotEmpty()) {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = errorMessage,
                            actionLabel = "Close"
                        )
                    }
                }
                Scaffold(
                    scaffoldState = scaffoldState,
                    snackbarHost = {
                       SnackbarHost(it) { data ->
                           Snackbar(
                               snackbarData = data,
                               backgroundColor = Color(0xFFFFE9E9),
                               contentColor = Color(0xFFDE4841),
                               actionColor = Color(0xFF03121A),
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
                                placeholder = {
                                    Text(text = "Enter City Name")
                                }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            if(isSearching) {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            } else {
                                if(cityData.isNotEmpty) {
                                    Text(
                                        text = cityData.toString()
                                    )
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}
