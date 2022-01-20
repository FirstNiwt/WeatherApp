package com.example.weatherapp.data.db.entity


import com.google.gson.annotations.SerializedName

data class Hourly(
    val clouds: Int,
    val dt: Int,
    val humidity: Int,
    val pop: Double,
    val pressure: Int,
    val temp: Double,
    val weather: List<Weather>,
    @SerializedName("wind_speed")
    val windSpeed: Double,
    var sunrise:Int = 0,
    var sunset:Int = 0,
    var timezone:Int = 0
)