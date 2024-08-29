package com.example.weatherapp.Api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherInterface {

    @GET("/v1/current.json")
    suspend fun  getWeather(
        @Query("key") apikey : String,
        @Query("q") cidade : String
    ) : Response<WeatherModel>
}