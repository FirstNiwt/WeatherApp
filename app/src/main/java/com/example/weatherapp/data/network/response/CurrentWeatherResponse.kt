package com.example.weatherapp.data.network.response

import com.example.weatherapp.data.db.entity.*
import com.google.gson.annotations.SerializedName

data class CurrentWeatherResponse(
    val base: String,
    val clouds: Clouds,
    val cod: Int,
    @SerializedName("coord")
    val coordinate: Coordinate,
    val dt: Int,
    val id: Int,
    val main: Main,
    val name: String,
    val sys: Sys,
    val timezone: Int,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)
