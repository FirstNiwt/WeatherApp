package com.example.weatherapp.data.provider

interface LocationProvider {
    suspend fun hasLocationChanged(lat:Double, lon:Double,cityName:String?): Boolean
    suspend fun getPreferredLocation():String
    fun isHomeAlert():Boolean
}