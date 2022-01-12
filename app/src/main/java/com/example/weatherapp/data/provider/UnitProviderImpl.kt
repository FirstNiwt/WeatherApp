package com.example.weatherapp.data.provider

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.weatherapp.internal.Units

class UnitProviderImpl(context: Context) : UnitProvider, PreferenceProvider(context) {


    override fun getUnitType(): Units {
        val selectedUnitType = preferences.getString("UNIT_SYSTEM", Units.METRIC.name)
        return Units.valueOf(selectedUnitType!!)
    }
}