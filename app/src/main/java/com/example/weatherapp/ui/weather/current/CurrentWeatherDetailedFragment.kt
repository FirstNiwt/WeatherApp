package com.example.weatherapp.ui.weather.current

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.weatherapp.R
import com.example.weatherapp.data.network.ConnectivityInterceptorImpl
import com.example.weatherapp.data.network.OpenWeatherApiService
import com.example.weatherapp.data.network.WeatherNetworkDataSourceImpl
import com.example.weatherapp.databinding.CurrentWeatherDetailedFragmentBinding
import com.example.weatherapp.databinding.FragmentHomeScreenBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CurrentWeatherDetailedFragment : Fragment() {
    private var _binding: CurrentWeatherDetailedFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = CurrentWeatherDetailedFragment()
    }

    private lateinit var viewModel: CurrentWeatherDetailedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CurrentWeatherDetailedFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CurrentWeatherDetailedViewModel::class.java)
        // TODO: Use the ViewModel
        val apiService = OpenWeatherApiService(ConnectivityInterceptorImpl(this.context!!))
        val weatherNetworkDataSource = WeatherNetworkDataSourceImpl(apiService)

        weatherNetworkDataSource.fetchedCurrentWeather.observe(this, {
            binding.textView.text = it.toString()
        })
        GlobalScope.launch(Dispatchers.Main) {
            weatherNetworkDataSource.fetchCurrentData("Olkusz","","")

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}