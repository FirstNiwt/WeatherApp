package com.example.weatherapp.ui.weather.weekly.detailed

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weatherapp.R

class WeeklyDetailWeatherFragment : Fragment() {

    companion object {
        fun newInstance() = WeeklyDetailWeatherFragment()
    }

    private lateinit var viewModel: WeeklyDetailWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.weekly_detail_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(WeeklyDetailWeatherViewModel::class.java)
        // TODO: Use the ViewModel
    }

}