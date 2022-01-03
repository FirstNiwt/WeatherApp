package com.example.weatherapp.data.db.entity


import com.google.gson.annotations.SerializedName

data class Daily(
    val clouds: Int,
    val dt: Int,
    val humidity: Int,
    val pop: Double,
    val pressure: Int,
    val rain: Double,
    val temp: Temp,
    val snow: Double,
    val sunrise: Int,
    val sunset: Int,
    val weather: List<Weather>,
    @SerializedName("wind_speed")
    val windSpeed: Double
)