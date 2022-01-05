package com.example.weatherapp.ui.weather.current

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.data.provider.UnitProvider
import com.example.weatherapp.data.repository.ForecastRepository


class CurrentWeatherViewModelFactory constructor(private val forecastRepository:ForecastRepository,private val unitProvider: UnitProvider):
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CurrentWeatherDetailedViewModel(forecastRepository,unitProvider) as T
    }

}