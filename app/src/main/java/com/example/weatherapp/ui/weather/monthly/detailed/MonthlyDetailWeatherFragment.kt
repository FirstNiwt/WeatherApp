package com.example.weatherapp.ui.weather.monthly.detailed

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weatherapp.R

class MonthlyDetailWeatherFragment : Fragment() {

    companion object {
        fun newInstance() = MonthlyDetailWeatherFragment()
    }

    private lateinit var viewModel: MonthlyDetailWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.monthly_detail_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MonthlyDetailWeatherViewModel::class.java)
        // TODO: Use the ViewModel
    }

}