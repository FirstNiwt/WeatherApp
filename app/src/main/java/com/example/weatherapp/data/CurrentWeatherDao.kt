package com.example.weatherapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.data.db.entity.CURRENT_WEATHER_ID
import com.example.weatherapp.data.db.entity.CurrentWeatherEntry


@Dao
interface CurrentWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeatherData(weatherEntry:CurrentWeatherEntry)

    @Query("select * from current_weather where keyId = $CURRENT_WEATHER_ID")
    fun readWeatherData(): LiveData<CurrentWeatherEntry>

    @Query("select coordinate_lat from current_weather where keyId = $CURRENT_WEATHER_ID ")
    fun getWeatherLocationLat():Double?

    @Query("select coordinate_lon from current_weather where keyId = $CURRENT_WEATHER_ID ")
    fun getWeatherLocationLon():Double?

    @Query("select dt from current_weather where keyId = $CURRENT_WEATHER_ID")
    fun getWeatherFetchTime():Int?

    @Query("select name from current_weather where keyId = $CURRENT_WEATHER_ID")
    fun getCityName():String?

}