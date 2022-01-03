package com.example.weatherapp.ui.weather.weekly

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.data.repository.ForecastRepository
import com.example.weatherapp.ui.weather.current.CurrentWeatherDetailedViewModel
import javax.inject.Inject

class DailyWeatherListFactory @Inject constructor(private val forecastRepository: ForecastRepository):
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DailyWeatherListViewModel(forecastRepository) as T
    }

}