package com.example.weatherapp.data.network

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.data.network.response.CurrentWeatherResponse
import com.example.weatherapp.internal.NoConnectivityException

class WeatherNetworkDataSourceImpl(private val openWeatherApiService: OpenWeatherApiService) : WeatherNetworkDataSource {
    private val _fetchedCurrentWeather = MutableLiveData<CurrentWeatherResponse>()

    override val fetchedCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _fetchedCurrentWeather

    override suspend fun fetchCurrentData(
        location: String,
        units: String,
        languageOfResponse: String
    ) {
       try {
           val currentWeather = openWeatherApiService
               .getCurrentWeatherData(location,units,languageOfResponse)
               .await()
           _fetchedCurrentWeather.postValue(currentWeather)
       }
       catch (e: NoConnectivityException)
       {
           Log.e("Connectivity", "No internet connection",e)
       }
    }
}