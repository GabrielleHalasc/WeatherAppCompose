package com.example.weatherapp.Api

sealed class InternetResponse<out T> {

    data class Success<out T>(val data : T) : InternetResponse<T>()
    data class Error(val message : String) : InternetResponse<Nothing>()
    object Loading : InternetResponse<Nothing>()
}