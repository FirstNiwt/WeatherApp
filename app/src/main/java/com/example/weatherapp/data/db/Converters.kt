package com.example.weatherapp.data.db

import androidx.room.TypeConverter
import com.example.weatherapp.data.db.entity.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class Converters {



    @TypeConverter
    fun fromListWeatherToString(weatherList: List<Weather>):String
    {
        return Gson().toJson(weatherList)
    }

    @TypeConverter
    fun fromStringToWeatherList(weatherString: String): List<Weather>
    {

        val listType: Type = object : TypeToken<List<Weather>>() {}.type

        return Gson().fromJson(weatherString,listType)
    }

    @TypeConverter
    fun fromListDailyToString(dailyList: List<Daily>):String
    {
        return Gson().toJson(dailyList)
    }

    @TypeConverter
    fun fromStringToDailyList(weatherString: String): List<Daily>
    {

        val listType: Type = object : TypeToken<List<Daily>>() {}.type

        return Gson().fromJson(weatherString,listType)
    }


    @TypeConverter
    fun fromListHourlyToString(hourlyList: List<Hourly>):String
    {
        return Gson().toJson(hourlyList)
    }

    @TypeConverter
    fun fromStringToHourlyList(weatherString: String): List<Hourly>
    {

        val listType: Type = object : TypeToken<List<Hourly>>() {}.type

        return Gson().fromJson(weatherString,listType)
    }

    @TypeConverter
    fun fromListAlertToString(hourlyList: List<Alert>):String
    {
        return Gson().toJson(hourlyList)
    }

    @TypeConverter
    fun fromStringToAlertList(weatherString: String): List<Alert>
    {
        val listType: Type = object : TypeToken<List<Alert>>() {}.type

        return Gson().fromJson(weatherString,listType)
    }





}