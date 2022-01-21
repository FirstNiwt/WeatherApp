package com.example.weatherapp.data.provider

interface LocationProvider {
    suspend fun hasLocationChanged(lat:Double, lon:Double,cityName:String?,language:String?): Boolean
    suspend fun getPreferredLocation():String
    fun isHomeAlert():Boolean
    fun getLanguage():String?

}