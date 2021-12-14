package com.example.weatherapp.data.db

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.weatherapp.data.db.entity.Weather
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*


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

}