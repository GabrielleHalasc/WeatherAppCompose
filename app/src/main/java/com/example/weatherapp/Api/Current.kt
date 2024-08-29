package com.example.weatherapp.Api

data class Current(
    val condition: Condition,
    val feelslike_c: String,
    val humidity: String,
    val precip_mm: String,
    val temp_c: String,
    val wind_kph: String,

)