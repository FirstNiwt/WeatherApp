package com.example.weatherapp.data.network

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.data.db.entity.CurrentWeatherEntry

import com.example.weatherapp.internal.NoConnectivityException
import javax.inject.Inject

class WeatherNetworkDataSourceImpl @Inject constructor (private val openWeatherApiService: OpenWeatherApiService) : WeatherNetworkDataSource {
    private val _fetchedCurrentWeather = MutableLiveData<CurrentWeatherEntry>()

    override val fetchedCurrentWeather: LiveData<CurrentWeatherEntry>
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