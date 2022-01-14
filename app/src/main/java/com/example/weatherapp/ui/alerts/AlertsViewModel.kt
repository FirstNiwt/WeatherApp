package com.example.weatherapp.ui.alerts

import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.provider.UnitProvider
import com.example.weatherapp.data.repository.ForecastRepository
import com.example.weatherapp.internal.lazyDeferred



class AlertsViewModel(private val forecastRepository: ForecastRepository, unitProvider: UnitProvider) : ViewModel() {

    private val units:String =  unitProvider.getUnitType().toString()

    val futureWeather by lazyDeferred {forecastRepository.getFutureWeather(units)}
    val currentWeather by lazyDeferred {forecastRepository.getCurrentWeather(units)}

}