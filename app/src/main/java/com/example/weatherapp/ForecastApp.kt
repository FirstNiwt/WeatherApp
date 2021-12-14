package com.example.weatherapp

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ForecastApp : Application(){


    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}