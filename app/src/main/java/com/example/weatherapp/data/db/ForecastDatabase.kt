package com.example.weatherapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherapp.data.CurrentWeatherDao
import com.example.weatherapp.data.db.entity.CurrentWeatherEntry

@Database(

    entities = [CurrentWeatherEntry::class],
    version = 1,
    exportSchema = false
)
abstract class ForecastDatabase: RoomDatabase() {

    abstract fun currentWeatherDao() : CurrentWeatherDao

    companion object {
        @Volatile private var instance: ForecastDatabase? = null
        private val STOP = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(STOP){
            instance ?: createDatabase(context).also{instance = it}
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(context.applicationContext,
        ForecastDatabase::class.java, "forecast.db")
            .build()
    }

}