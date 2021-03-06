package com.example.weatherapp.data.db.entity

import androidx.room.*
import com.google.gson.annotations.SerializedName

const val CURRENT_WEATHER_ID = 0


@Entity(tableName = "current_weather")
data class CurrentWeatherEntry(
    val base: String,
    @Embedded(prefix = "clouds_")
    val clouds: Clouds,
    val cod: Int,
    @SerializedName("coord")
    @Embedded(prefix = "coordinate_")
    val coordinate: Coordinate,
    val dt: Int,
    val id: Int,
    @Embedded(prefix = "main_")
    val main: Main,
    val name: String,
    @Embedded(prefix = "sys_")
    val sys: Sys,
    val timezone: Int,
    val visibility: Int,
    val weather: List<Weather>,
    @Embedded(prefix = "wind_")
    val wind: Wind,
    var languageOfResponse:String = "ENGLISH"
) {

    @PrimaryKey(autoGenerate = false)
    var keyId: Int = CURRENT_WEATHER_ID

    constructor() :this("", Clouds(0), 0, Coordinate(0.0,0.0), 0, 0,
        Main(0.0,0,0,0.0,0.0,0.0),
        "",Sys("",0,0.0,0,0,0),0,
        0, mutableListOf(Weather("",0)),Wind(0,0.0))
}