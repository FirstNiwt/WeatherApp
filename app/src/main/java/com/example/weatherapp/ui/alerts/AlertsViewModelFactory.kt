package com.example.weatherapp.ui.alerts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.data.provider.UnitProvider
import com.example.weatherapp.data.repository.ForecastRepository


class AlertsViewModelFactory constructor(private val forecastRepository: ForecastRepository, private val unitProvider: UnitProvider):
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AlertsViewModel(forecastRepository, unitProvider) as T
    }

}