package com.example.weatherapp.data.provider

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.weatherapp.internal.Units

class UnitProviderImpl(context: Context) : UnitProvider {
    private val appContext = context.applicationContext
    private val preferences: SharedPreferences
    get() = PreferenceManager.getDefaultSharedPreferences(appContext)

    override fun getUnitType(): Units {
        val selectedUnitType = preferences.getString("UNIT_SYSTEM", Units.METRIC.name)
        return Units.valueOf(selectedUnitType!!)
    }
}