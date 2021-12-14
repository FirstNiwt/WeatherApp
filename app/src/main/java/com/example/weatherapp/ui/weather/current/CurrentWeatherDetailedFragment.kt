package com.example.weatherapp.ui.weather.current

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.weatherapp.R
import com.example.weatherapp.data.network.ConnectivityInterceptorImpl
import com.example.weatherapp.data.network.OpenWeatherApiService
import com.example.weatherapp.data.network.WeatherNetworkDataSourceImpl
import com.example.weatherapp.data.repository.ForecastRepository
import com.example.weatherapp.data.repository.ForecastRepositoryImpl
import com.example.weatherapp.databinding.CurrentWeatherDetailedFragmentBinding
import com.example.weatherapp.databinding.FragmentHomeScreenBinding
import com.example.weatherapp.ui.base.ScopedFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CurrentWeatherDetailedFragment : ScopedFragment() {
    @Inject lateinit var apiService: OpenWeatherApiService
    @Inject lateinit var weatherNetworkDataSource:WeatherNetworkDataSourceImpl
    @Inject lateinit var viewModelFactory:CurrentWeatherViewModelFactory
    @Inject lateinit var forecastRepository: ForecastRepository


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
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(CurrentWeatherDetailedViewModel::class.java)

       bindUI()

        //val apiService = OpenWeatherApiService(ConnectivityInterceptorImpl(this.context!!))
        //val weatherNetworkDataSource = WeatherNetworkDataSourceImpl(apiService)
       // weatherNetworkDataSource.fetchedCurrentWeather.observe(this, {
       //     binding.textView.text = it.toString()
       // })
       // GlobalScope.launch(Dispatchers.Main) {
       //     forecastRepository.getCurrentWeather("metric")
       // }
        // }
    }

    private fun bindUI() = launch{

        val weather = viewModel.currentWeather.await()

        weather.observe(this@CurrentWeatherDetailedFragment, Observer {
            if(it == null) {
                Toast.makeText(context,"Not initialized",Toast.LENGTH_SHORT).show()
                return@Observer
            }
            binding.textView.text = it.toString()

            })

    }
    override fun onDestroyView() {
        super.onDestroyView()
    }
}