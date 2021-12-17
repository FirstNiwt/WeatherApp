package com.example.weatherapp.data.repository

import androidx.lifecycle.LiveData
import com.example.weatherapp.data.db.entity.CurrentWeatherEntry
import com.example.weatherapp.data.db.entity.FutureWeatherEntry

interface ForecastRepository {

    suspend fun getCurrentWeather(units: String): LiveData<CurrentWeatherEntry>
    suspend fun getFutureWeather(units: String): LiveData<FutureWeatherEntry>

}