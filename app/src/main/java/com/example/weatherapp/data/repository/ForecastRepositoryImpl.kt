package com.example.weatherapp.data.repository

import android.widget.Toast
import androidx.lifecycle.LiveData
import com.example.weatherapp.data.CurrentWeatherDao
import com.example.weatherapp.data.FutureWeatherDao
import com.example.weatherapp.data.db.entity.CurrentWeatherEntry
import com.example.weatherapp.data.db.entity.FutureWeatherEntry
import com.example.weatherapp.data.network.WeatherNetworkDataSource
import com.example.weatherapp.data.provider.LocationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*


class ForecastRepositoryImpl constructor(
    private val currentWeatherDao:CurrentWeatherDao,
    private val futureWeatherDao: FutureWeatherDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource,
    private val locationProvider:LocationProvider
) : ForecastRepository {
    init{
        weatherNetworkDataSource.apply{
            fetchedCurrentWeather.observeForever { newCurrentWeather ->
                preserveCurrentWeather(newCurrentWeather)
            }

            fetchedFutureWeather.observeForever{ newFutureWeather ->
                preserveFutureWeather(newFutureWeather)
            }

        }
    }
    override suspend fun getCurrentWeather(units: String): LiveData<CurrentWeatherEntry> {
       return withContext(Dispatchers.IO){
           initWeatherData(units)
           return@withContext currentWeatherDao.readWeatherData()
       }

    }


    override suspend fun getFutureWeather(units: String): LiveData<FutureWeatherEntry> {
       return withContext(Dispatchers.IO){
       initWeatherData(units)
           return@withContext futureWeatherDao.readWeatherData()
       }
    }

    private suspend fun initWeatherData(units: String) {
        fetchFutureWeather(units)
        val lastLat:Double? = currentWeatherDao.getWeatherLocationLat()
        val lastLon:Double? = currentWeatherDao.getWeatherLocationLon()
        val lastFetchSeconds:Int? = currentWeatherDao.getWeatherFetchTime()
        val cityName:String? = currentWeatherDao.getCityName()



        val i = lastFetchSeconds?.let { Instant.ofEpochSecond(it.toLong()) }
        val lastFetchTime = ZonedDateTime.ofInstant(i,ZoneOffset.UTC)

        if((lastLat == null || lastLon == null)
            || locationProvider.hasLocationChanged(lastLat,lastLon,cityName)
                )
        {
            //fetchFutureWeather(units)

        }
        //if(isFetchNeeded(lastFetchTime))
            //fetchFutureWeather(units)

    }

    private fun isFetchNeeded(lastFetchTime:ZonedDateTime):Boolean{

        val fifteenMinutesAgo = Instant.now().minusSeconds(900)
        return lastFetchTime.isBefore(ZonedDateTime.ofInstant(fifteenMinutesAgo,ZoneOffset.UTC))

    }

    private suspend fun fetchCurrentWeather(units: String, city:String){

            weatherNetworkDataSource.fetchCurrentDataByLocation(city,units,"en")

    }

    private suspend fun fetchFutureWeather(units: String){
        val preferredString:String = locationProvider.getPreferredLocation()
        val preferredLocation:List<String> = preferredString.split(",")

        if(preferredLocation.size == 2) //That means that we are using device location cuz we are using lat and lon
        {
            weatherNetworkDataSource.fetchFutureDataByCoordinates(preferredLocation[0].toDouble(),
                preferredLocation[1].toDouble(),"minutely",units,"en")

            weatherNetworkDataSource.fetchCurrentDataByCoordinates(preferredLocation[0].toDouble(),
                preferredLocation[1].toDouble(),units,"en")

        }

        else{  //Means we are using location set up in app settings
            fetchCurrentWeather(units,preferredLocation[0])

            weatherNetworkDataSource.fetchFutureDataByCoordinates(currentWeatherDao.getWeatherLocationLat(),
                currentWeatherDao.getWeatherLocationLon(),"minutely",units,"en")

        }


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