package com.example.weatherapp.ui

import android.annotation.SuppressLint
import androidx.lifecycle.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest

class LifecycleBoundLocationManager(lifecycleOwner:LifecycleOwner,private val fusedLocationProviderClient:
FusedLocationProviderClient,private val locationCallback: LocationCallback) : LifecycleObserver {

    init{
        lifecycleOwner.lifecycle.addObserver(this)
    }

    private val locationRequest = LocationRequest.create().apply{
        interval = 5000
        fastestInterval = 10 * 60 * 1000
        priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    @SuppressLint("MissingPermission")
    fun startLocationUpdate()
    {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, null)
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun removeLocationUpdates(){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }
}