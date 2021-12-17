package com.example.weatherapp.data.db

import android.content.Context
import androidx.room.*
import com.example.weatherapp.data.CurrentWeatherDao
import com.example.weatherapp.data.FutureWeatherDao
import com.example.weatherapp.data.db.entity.CurrentWeatherEntry
import com.example.weatherapp.data.db.entity.FutureWeatherEntry

@Database(

    entities = [CurrentWeatherEntry::class,FutureWeatherEntry::class],
    version = 1
)

@TypeConverters(Converters::class)
abstract class ForecastDatabase: RoomDatabase() {

    abstract fun getCurrentWeatherDao() : CurrentWeatherDao
    abstract fun getFutureWeatherDao() : FutureWeatherDao

    companion object {
        @Volatile private var instance: ForecastDatabase? = null
        private val STOP = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(STOP){
            instance ?: createDatabase(context).also{instance = it}
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(context.applicationContext,
        ForecastDatabase::class.java, "1.db")
            .build()

    }

}