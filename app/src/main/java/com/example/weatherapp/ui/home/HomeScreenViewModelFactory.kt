package com.example.weatherapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.data.provider.UnitProvider
import com.example.weatherapp.data.repository.ForecastRepository
import com.example.weatherapp.ui.weather.current.CurrentWeatherDetailedViewModel

class HomeScreenViewModelFactory constructor(private val forecastRepository: ForecastRepository, private val unitProvider: UnitProvider):
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeScreenViewModel(forecastRepository,unitProvider) as T
    }

}