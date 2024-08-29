package com.example.weatherapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.Api.InternetResponse
import com.example.weatherapp.Api.RetrofitInstance
import com.example.weatherapp.Api.WeatherApi
import com.example.weatherapp.Api.WeatherModel
import kotlinx.coroutines.launch

class WeatherViewModel: ViewModel() {

    private val weatherApi = RetrofitInstance.weatherApi
    private val _weatherLiveData = MutableLiveData<InternetResponse<WeatherModel>>()
    val weatherLiveData : LiveData<InternetResponse<WeatherModel>> = _weatherLiveData

    fun getData(cidade :String){

        _weatherLiveData.value = InternetResponse.Loading

        viewModelScope.launch {
            try {
                val response = weatherApi.getWeather(WeatherApi.apiKey,cidade)
                if(response.isSuccessful){
                    response.body()?.let {
                        _weatherLiveData.value = InternetResponse.Success(it)
                    }
                }else{
                    _weatherLiveData.value = InternetResponse.Error("Erro ao carregar")
                }
            }
            catch (e : Exception){
                _weatherLiveData.value = InternetResponse.Error("Erro ao carregar")
            }

        }

    }
}