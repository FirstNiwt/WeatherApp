package com.example.weatherapp.data.provider

class LocationProviderImpl : LocationProvider {
    override suspend fun hasLocationChanged(lat: Double, lon: Double): Boolean {
        return false //TODO HARDCODED
    }

    override suspend fun getPreferredLocation(): String {
        return "Olkusz"  //TODO HARDCODED
    }
}