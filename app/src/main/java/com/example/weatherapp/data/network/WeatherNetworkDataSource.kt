package com.example.weatherapp.data.network

import androidx.lifecycle.LiveData

import com.example.weatherapp.data.network.response.CurrentWeatherResponse

interface WeatherNetworkDataSource {

    val fetchedCurrentWeather: LiveData<CurrentWeatherResponse>

    suspend fun fetchCurrentData(location:String, units:String, languageOfResponse:String)
}