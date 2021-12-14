package com.example.weatherapp.data.repository

import androidx.lifecycle.LiveData
import com.example.weatherapp.data.CurrentWeatherDao
import com.example.weatherapp.data.db.entity.CurrentWeatherEntry
import com.example.weatherapp.data.network.WeatherNetworkDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZonedDateTime
import javax.inject.Inject

class ForecastRepositoryImpl @Inject constructor(
    private val currentWeatherDao:CurrentWeatherDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource
) : ForecastRepository {
    init{
        weatherNetworkDataSource.fetchedCurrentWeather.observeForever {newCurrentWeather ->
            persevereCurrentWeather(newCurrentWeather)
        }
    }
    override suspend fun getCurrentWeather(units: String): LiveData<CurrentWeatherEntry> {
       return withContext(Dispatchers.IO){
           initWeatherData()
           return@withContext currentWeatherDao.readWeatherData()
       }

    }

    private suspend fun initWeatherData() {

            fetchCurrentWeather()

    }

    private fun isFetchNeeded(lastFetchTime:LocalTime):Boolean{

        val fifteenMinutesAgo = ZonedDateTime.now().minusMinutes(15).toLocalTime()
        return lastFetchTime.isBefore(fifteenMinutesAgo)

    }

    private suspend fun fetchCurrentWeather(){
        weatherNetworkDataSource.fetchCurrentData("Kraków","metric","en")
    }

    private fun persevereCurrentWeather(currentWeather:CurrentWeatherEntry){

        GlobalScope.launch(Dispatchers.IO) {

            currentWeatherDao.insertWeatherData(currentWeather)

        }

    }

}