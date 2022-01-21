package com.example.weatherapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
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

    @Query("select languageOfResponse from current_weather where keyId = $CURRENT_WEATHER_ID")
    fun getLanguage():String?

    @Query("update current_weather set languageOfResponse=:language where keyId = $CURRENT_WEATHER_ID")
    fun updateLang(language:String?)

    @Query("update current_weather set name=:cityName where keyId= $CURRENT_WEATHER_ID")
    fun updateCityName(cityName:String?)

}