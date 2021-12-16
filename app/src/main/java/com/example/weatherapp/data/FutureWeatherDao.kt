package com.example.weatherapp.data

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.data.db.entity.FUTURE_WEATHER_ID
import com.example.weatherapp.data.db.entity.FutureWeatherEntry

interface FutureWeatherDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeatherData(weatherEntry: FutureWeatherEntry)

    @Query("select * from future_weather where keyId = $FUTURE_WEATHER_ID")
    fun readWeatherData(): LiveData<FutureWeatherEntry>

}