package com.example.weatherapp.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.data.db.entity.CurrentWeatherEntry
import com.example.weatherapp.data.db.entity.FutureWeatherEntry

import com.example.weatherapp.internal.NoConnectivityException


class WeatherNetworkDataSourceImpl constructor (private val openWeatherApiService: OpenWeatherApiService) : WeatherNetworkDataSource {
    private val _fetchedCurrentWeather = MutableLiveData<CurrentWeatherEntry>()
    private val _fetchedFutureWeather = MutableLiveData<FutureWeatherEntry>()

    override val fetchedCurrentWeather: LiveData<CurrentWeatherEntry>
        get() = _fetchedCurrentWeather

    override val fetchedFutureWeather: LiveData<FutureWeatherEntry>
        get() = _fetchedFutureWeather

    override suspend fun fetchCurrentDataByLocation(
        location: String,
        units: String,
        languageOfResponse: String
    ) {
       try {
           val currentWeather = openWeatherApiService
               .getCurrentWeatherDataByLocationAsync(location,units,languageOfResponse)
               .await()
           _fetchedCurrentWeather.postValue(currentWeather)
       }
       catch (e: NoConnectivityException)
       {
           Log.e("Connectivity", "No internet connection",e)
       }
    }

    override suspend fun fetchCurrentDataByCoordinates(
        lat: Double,
        lon: Double,
        units: String,
        languageOfResponse: String
    ) {
        try{
            val currentWeather = openWeatherApiService
                .getCurrentWeatherDataByCoordinatesAsync(lat,lon,units,languageOfResponse)
                .await()
            _fetchedCurrentWeather.postValue(currentWeather)
        }
        catch (e: NoConnectivityException)
        {
            Log.e("Connectivity", "No internet connection",e)
        }
    }


    override suspend fun fetchFutureDataByCoordinates(
        lat: Double?,
        lon: Double?,
        exclude: String,
        units: String,
        languageOfResponse: String
    ) {
    try {
            val futureWeather = openWeatherApiService
                .getFutureWeatherDataByCoordinatesAsync(lat, lon,exclude,units, languageOfResponse)
                .await()
            _fetchedFutureWeather.postValue(futureWeather)
        }

    catch(e: NoConnectivityException)
    {
        Log.e("Connectivity", "No internet connection",e)
    }

}

}