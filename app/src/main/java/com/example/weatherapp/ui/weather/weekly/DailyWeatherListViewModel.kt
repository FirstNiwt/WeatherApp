package com.example.weatherapp.ui.weather.weekly
import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.provider.UnitProvider
import com.example.weatherapp.data.repository.ForecastRepository
import com.example.weatherapp.internal.Units
import com.example.weatherapp.internal.lazyDeferred
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class DailyWeatherListViewModel(private val forecastRepository: ForecastRepository,unitProvider: UnitProvider) : ViewModel() {

    private val units:String =  unitProvider.getUnitType().toString()

    val futureWeather by lazyDeferred {forecastRepository.getFutureWeather(units)}

}