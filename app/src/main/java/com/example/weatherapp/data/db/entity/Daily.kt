package com.example.weatherapp.data.db.entity


import androidx.room.Embedded
import com.google.gson.annotations.SerializedName

data class Daily(
    val clouds: Int,
    val dt: Int,
    val humidity: Int,
    val pop: Double,
    @Embedded(prefix = "feels_like_")
    val feels_like:FeelsLike,
    val pressure: Int,
    val rain: Double,
    val uvi:Double,
    val temp: Temp,
    val snow: Double,
    val sunrise: Int,
    val sunset: Int,
    val weather: List<Weather>,
    @SerializedName("wind_speed")
    val windSpeed: Double,
    val expanded: Boolean = false
)