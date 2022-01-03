package com.example.weatherapp.ui.weather.weekly

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.data.db.entity.Daily
import com.example.weatherapp.data.db.entity.Hourly
import com.example.weatherapp.data.network.OpenWeatherApiService
import com.example.weatherapp.data.network.WeatherNetworkDataSourceImpl
import com.example.weatherapp.data.repository.ForecastRepository
import com.example.weatherapp.databinding.CurrentWeatherDetailedFragmentBinding
import com.example.weatherapp.databinding.FragmentDailyWeatherListBinding
import com.example.weatherapp.ui.base.ScopedFragment
import com.example.weatherapp.ui.weather.current.CurrentWeatherDetailedFragment
import com.example.weatherapp.ui.weather.current.CurrentWeatherDetailedViewModel
import com.example.weatherapp.ui.weather.current.CurrentWeatherViewModelFactory
import com.example.weatherapp.ui.weather.current.HourlyWeatherAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DailyWeatherListFragment : ScopedFragment() {
    @Inject lateinit var forecastRepository: ForecastRepository
    @Inject lateinit var apiService: OpenWeatherApiService
    @Inject lateinit var weatherNetworkDataSource: WeatherNetworkDataSourceImpl
    @Inject lateinit var viewModelFactory: DailyWeatherListFactory
    private lateinit var dailyAdapter: DailyListWeatherAdapter

    private var _binding: FragmentDailyWeatherListBinding? = null
    private val binding get() = _binding!!
    private var linearLayoutManager = LinearLayoutManager(activity,
        LinearLayoutManager.VERTICAL,false)

    companion object{
        fun newInstance() = DailyWeatherListFragment()
    }

    private lateinit var viewModel: DailyWeatherListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDailyWeatherListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[DailyWeatherListViewModel::class.java]

        bindUI()

    }

    private fun bindUI() = launch{

        val futureWeather = viewModel.futureWeather.await()




        futureWeather.observe(this@DailyWeatherListFragment, Observer{
            if(it == null)
            {
                Toast.makeText(context,"Not initialized", Toast.LENGTH_SHORT).show()
                return@Observer
            }


            //binding.textViewHourlyLoading.text = it.toString()
            binding.mainRecyclerView.layoutManager = linearLayoutManager

            val dailyWeatherMutableList:MutableList<Daily> = it.daily.toMutableList()



            dailyAdapter = DailyListWeatherAdapter(dailyWeatherMutableList)
            binding.mainRecyclerView.adapter = dailyAdapter

        })

    }
}