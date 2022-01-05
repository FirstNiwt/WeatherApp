package com.example.weatherapp.data.provider

import com.example.weatherapp.internal.Units

interface UnitProvider {
    fun getUnitType():Units
}