package com.example.weatherapp.ui.weather.current

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.data.repository.ForecastRepository
import javax.inject.Inject

class CurrentWeatherViewModelFactory @Inject constructor(private val forecastRepository:ForecastRepository):ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CurrentWeatherDetailedViewModel(forecastRepository) as T
    }

}