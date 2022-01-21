package com.example.weatherapp.data.provider

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.example.weatherapp.internal.LocationPermissionNotGrantedException
import com.example.weatherapp.internal.asDeferred
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Deferred
import javax.inject.Inject
import kotlin.math.abs

const val USE_DEVICE_LOCATION = "USE_DEVICE_LOCATION"
const val CUSTOM_LOCATION = "CUSTOM_LOCATION"
const val CUSTOM_LANGUAGE = "LANGUAGE"

class LocationProviderImpl (private val fusedLocationProviderClient: FusedLocationProviderClient, context: Context)
    :PreferenceProvider(context), LocationProvider {
    private val appContext = context.applicationContext

    override suspend fun hasLocationChanged(lat: Double, lon: Double, cityName:String?, language:String?): Boolean {
        val deviceLocationChanged = try{hasDeviceLocationChanged(lat,lon)
        }catch(e: LocationPermissionNotGrantedException) {false}

        val preferredLocation = getPreferredLocation()
        val preferredLocationList:List<String> = preferredLocation.split(",")

        return if(preferredLocationList.size == 1){
            (deviceLocationChanged || hasCustomLocationChanged(cityName) || hasLanguageChanged(language))
        } else{
            (deviceLocationChanged||hasLanguageChanged(language))
        }

    }

    override suspend fun getPreferredLocation(): String {
        if(isUsingDeviceLocation()){
            try {
                val deviceLocation = getLastDeviceLocationAsync().await()
                    ?: return "${getCustomLocationName()}"

                return "${deviceLocation.latitude},${deviceLocation.longitude}"
            }catch(e: LocationPermissionNotGrantedException){
                return "${getCustomLocationName()}"
            }

        }
        return "${getCustomLocationName()}"
    }


    override fun getLanguage(): String? {
        return preferences.getString(CUSTOM_LANGUAGE,null)
    }
    override fun isHomeAlert():Boolean
    {
        return preferences.getBoolean("SHOW_ALERTS",true)
    }

    private suspend fun hasDeviceLocationChanged(lat:Double, lon:Double):Boolean{
        if(!isUsingDeviceLocation())
            return false

        val deviceLocation = getLastDeviceLocationAsync().await()
            ?: return false

        val comparisonThreshold = 0.02

        return abs(deviceLocation.latitude - lat) > comparisonThreshold &&
                abs(deviceLocation.longitude - lon) >comparisonThreshold

    }
    private fun hasLanguageChanged(lang:String?):Boolean{
        val customLang = getCustomLanguage()
        return lang != customLang
    }

    private fun hasCustomLocationChanged(cityName: String?):Boolean{
        val customLocation = getCustomLocationName()
        return customLocation != cityName
    }


    private fun getCustomLocationName():String?{
        return preferences.getString(CUSTOM_LOCATION,null)
    }

    private fun getCustomLanguage():String?{
        return preferences.getString(CUSTOM_LANGUAGE,null)
    }

    private fun isUsingDeviceLocation():Boolean{
        return preferences.getBoolean(USE_DEVICE_LOCATION,true)
    }
    @SuppressLint("MissingPermission")
    private fun getLastDeviceLocationAsync(): Deferred<Location?>{
        return if(hasLocationPermission())
            fusedLocationProviderClient.lastLocation.asDeferred()
        else
        {
            throw LocationPermissionNotGrantedException()
        }
    }

    private fun hasLocationPermission():Boolean{
        return ContextCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
    }
}