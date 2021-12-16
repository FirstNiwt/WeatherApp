package com.example.weatherapp.data.db.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


const val FUTURE_WEATHER_ID = 1

@Entity(tableName = "future_weather")
data class FutureWeatherEntry(
    val daily: List<Daily>,
    val hourly: List<Hourly>,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    @SerializedName("timezone_offset")
    val timezoneOffset: Int
)
{
    @PrimaryKey(autoGenerate = false)
    var keyId: Int = FUTURE_WEATHER_ID

    constructor() :this(mutableListOf(Daily(0,0,0,0.0,0,0.0,
             0.0,0,0,mutableListOf(Weather("")),0.0)),
             mutableListOf(Hourly(0,0,0,0,0,0.0,
             mutableListOf(Weather("")),0.0)),0.0,0.0,"",0)

}