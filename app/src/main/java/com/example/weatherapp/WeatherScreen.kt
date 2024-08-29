package com.example.weatherapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weatherapp.Api.InternetResponse
import com.example.weatherapp.Api.WeatherModel
import com.example.weatherapp.ui.theme.Grey500
import com.example.weatherapp.ui.theme.White300


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(viewModel: WeatherViewModel, data: WeatherModel) {

    var cidade by remember { mutableStateOf("") }
    val weatherLiveData = viewModel.weatherLiveData.observeAsState()

    val backgroundPainter: Painter = when (data.current.condition.text.lowercase()) {
        "Sunny" -> painterResource(id = R.drawable.sunny_days)
        "Clear" -> painterResource(id = R.drawable.sunny_days)
        "Partly cloudy" -> painterResource(id = R.drawable.sunnycloud)
        "cloudy" -> painterResource(id = R.drawable.cloud_day)
        "rain" -> painterResource(id = R.drawable.drizzle_days)
        "snow" -> painterResource(id = R.drawable.snow_days)
        "thunderstorm" -> painterResource(id = R.drawable.thunderstorm)

        else -> painterResource(id = R.drawable.all_in_one)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = backgroundPainter,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedTextField(
                    value = cidade,
                    onValueChange = { cidade = it },
                    placeholder = { Text(text = "Busque por uma cidade") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            White300.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                    ),
                    shape = RoundedCornerShape(16.dp),
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = "Buscar",
                            modifier = Modifier
                                .size(20.dp)
                                .clickable {
                                    viewModel.getData(cidade)
                                },
                            tint = White300
                        )
                    }
                )
            }
            when (val result = weatherLiveData.value) {
                is InternetResponse.Error -> {
                    Text(
                        text = result.message
                    )
                }

                InternetResponse.Loading -> {
                    CircularProgressIndicator()
                }

                is InternetResponse.Success -> {
                    WeatherDetail(data = result.data)

                }

                null -> {}
            }
        }
    }


}


@Composable
fun WeatherDetail(data: WeatherModel) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        Text(text = "Localização", fontSize = 40.sp ,
            color = White300)

        Text(
            text = data.location.name, color = White300,
            fontSize = 18.sp
        )
        Text(
            text = "${data.current.temp_c}°",
            color = White300,
            fontSize = 56.sp,
            fontWeight = FontWeight.Bold
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AsyncImage(
                modifier = Modifier.size(35.dp),
                model = "https:${data.current.condition.icon}".replace("64x64", "128x128"),
                contentDescription = "Condition icon",

            )
            Text(
                text = data.current.condition.text,
                fontSize = 18.sp,
                color = White300,
            )

        }

        Spacer(modifier = Modifier.height(40.dp))

        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherCard(value = "Sensação Termica", key = "${data.current.feelslike_c}°")
                    WeatherCard(value = "Umidade", key = "${data.current.humidity}%")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherCard(value = "Vento", key = "${data.current.wind_kph} Km/H")
                    WeatherCard(value = "Chuva", key = "${data.current.precip_mm}mm")
                }
            }

        }
    }
}

@Composable
fun WeatherCard(value: String, key: String) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = key, fontWeight = FontWeight.Bold, color = White300,)
        Text(text = value, fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = White300,)
    }
}
