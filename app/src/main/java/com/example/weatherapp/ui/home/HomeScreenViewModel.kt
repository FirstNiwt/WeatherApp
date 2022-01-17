package com.example.weatherapp.ui.home

import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.provider.UnitProvider
import com.example.weatherapp.data.repository.ForecastRepository
import com.example.weatherapp.internal.lazyDeferred

class HomeScreenViewModel(private val forecastRepository: ForecastRepository, unitProvider: UnitProvider) : ViewModel()
    {

        private val units:String =  unitProvider.getUnitType().toString()
        val currentWeather by lazyDeferred { forecastRepository.getCurrentWeather(units)}
        val futureWeather by lazyDeferred{forecastRepository.getFutureWeather(units)}
}