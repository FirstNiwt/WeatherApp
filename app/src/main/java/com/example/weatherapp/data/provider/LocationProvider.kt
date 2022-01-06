package com.example.weatherapp.data.provider

interface LocationProvider {
    suspend fun hasLocationChanged(lat:Double, lon:Double): Boolean
    suspend fun getPreferredLocation():String
}