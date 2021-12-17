package com.example.weatherapp.data.repository

import androidx.lifecycle.LiveData
import com.example.weatherapp.data.CurrentWeatherDao
import com.example.weatherapp.data.FutureWeatherDao
import com.example.weatherapp.data.db.entity.CurrentWeatherEntry
import com.example.weatherapp.data.db.entity.FutureWeatherEntry
import com.example.weatherapp.data.network.WeatherNetworkDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZonedDateTime
import java.util.concurrent.Future
import javax.inject.Inject

class ForecastRepositoryImpl @Inject constructor(
    private val currentWeatherDao:CurrentWeatherDao,
    private val futureWeatherDao: FutureWeatherDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource
) : ForecastRepository {
    init{
        weatherNetworkDataSource.apply{
            fetchedCurrentWeather.observeForever { newCurrentWeather ->
                preserveCurrentWeather(newCurrentWeather)
            }

            fetchedFutureWeather.observeForever{
                newFutureWeather ->
                preserveFutureWeather(newFutureWeather)
            }

        }
    }
    override suspend fun getCurrentWeather(units: String): LiveData<CurrentWeatherEntry> {
       return withContext(Dispatchers.IO){
           initWeatherData()
           return@withContext currentWeatherDao.readWeatherData()
       }

    }


    override suspend fun getFutureWeather(units: String): LiveData<FutureWeatherEntry> {
       return withContext(Dispatchers.IO){
           initWeatherData()
           return@withContext futureWeatherDao.readWeatherData()
       }
    }

    private suspend fun initWeatherData() {

            //fetchCurrentWeather()
            fetchFutureWeather()
    }

    private fun isFetchNeeded(lastFetchTime:LocalTime):Boolean{

        val fifteenMinutesAgo = ZonedDateTime.now().minusMinutes(15).toLocalTime()
        return lastFetchTime.isBefore(fifteenMinutesAgo)

    }

    private suspend fun fetchCurrentWeather(){
        weatherNetworkDataSource.fetchCurrentData("Olkusz","metric","en")
    }

    private suspend fun fetchFutureWeather(){
        weatherNetworkDataSource.fetchFutureData(50.2813,19.56503,"minutely,alerts","metric","en")
    }

    private fun preserveCurrentWeather(currentWeather:CurrentWeatherEntry){

        GlobalScope.launch(Dispatchers.IO) {

            currentWeatherDao.insertWeatherData(currentWeather)

        }

    }
    private fun preserveFutureWeather(futureWeather:FutureWeatherEntry){
        GlobalScope.launch(Dispatchers.IO){
            futureWeatherDao.insertWeatherData(futureWeather)
        }

    }

}