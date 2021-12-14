package com.example.weatherapp.data.network

import androidx.lifecycle.LiveData
import com.example.weatherapp.data.db.entity.CurrentWeatherEntry



interface WeatherNetworkDataSource {

    val fetchedCurrentWeather: LiveData<CurrentWeatherEntry>

    suspend fun fetchCurrentData(location:String, units:String, languageOfResponse:String)
}