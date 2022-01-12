package com.example.weatherapp.data.network

import androidx.lifecycle.LiveData
import com.example.weatherapp.data.db.entity.CurrentWeatherEntry
import com.example.weatherapp.data.db.entity.FutureWeatherEntry


interface WeatherNetworkDataSource {

    val fetchedCurrentWeather: LiveData<CurrentWeatherEntry>
    val fetchedFutureWeather: LiveData<FutureWeatherEntry>


    suspend fun fetchCurrentDataByLocation(location:String, units:String, languageOfResponse:String)
    suspend fun fetchCurrentDataByCoordinates(lat:Double,lon: Double,units: String,languageOfResponse: String)
    suspend fun fetchFutureDataByCoordinates(lat:Double?, lon:Double?, exclude:String, units:String, languageOfResponse: String)

}