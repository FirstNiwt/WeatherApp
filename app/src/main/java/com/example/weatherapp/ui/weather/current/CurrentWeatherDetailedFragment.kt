package com.example.weatherapp.ui.weather.current

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.data.db.entity.Hourly
import com.example.weatherapp.data.network.OpenWeatherApiService
import com.example.weatherapp.data.network.WeatherNetworkDataSourceImpl
import com.example.weatherapp.data.provider.UnitProvider
import com.example.weatherapp.data.repository.ForecastRepository
import com.example.weatherapp.databinding.CurrentWeatherDetailedFragmentBinding
import com.example.weatherapp.ui.base.ScopedFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt



@AndroidEntryPoint
class CurrentWeatherDetailedFragment : ScopedFragment() {
    @Inject lateinit var apiService: OpenWeatherApiService
    @Inject lateinit var weatherNetworkDataSource:WeatherNetworkDataSourceImpl
    @Inject lateinit var viewModelFactory:CurrentWeatherViewModelFactory
    @Inject lateinit var forecastRepository: ForecastRepository
    @Inject lateinit var unitProvider: UnitProvider
    private lateinit var hourlyAdapter: HourlyWeatherAdapter

    private var _binding: CurrentWeatherDetailedFragmentBinding? = null
    private val binding get() = _binding!!
    private var linearLayoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)

    companion object {
        fun newInstance() = CurrentWeatherDetailedFragment()
    }

    private lateinit var viewModel: CurrentWeatherDetailedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CurrentWeatherDetailedFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[CurrentWeatherDetailedViewModel::class.java]
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "piątek, 17 grudnia 2021"
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Olkusz"
        (activity as? AppCompatActivity)?.supportActionBar?.show()

       bindUI()

    }

    private fun bindUI() = launch{

        val currentWeather = viewModel.currentWeather.await()
        val futureWeather = viewModel.futureWeather.await()

        currentWeather.observe(this@CurrentWeatherDetailedFragment, Observer {
            if(it == null) {
                Toast.makeText(context,"Not initialized",Toast.LENGTH_SHORT).show()
                return@Observer


            }


            binding.groupLoadingCurrent.visibility = View.GONE


            setWeatherDescription(0)
            setWeatherImage(it.weather[0].id)
            setTemperature(it.main.temp)

            showHumidityImage()
            setHumidity(it.main.humidity)

            showCloudinessImage()
            setCloudiness(it.clouds.all)


            showWindImage()
            setWindSpeed(it.wind.speed,unitProvider.getUnitType().toString())



            })

        futureWeather.observe(this@CurrentWeatherDetailedFragment, Observer{
            if(it == null)
            {
                Toast.makeText(context,"Not initialized",Toast.LENGTH_SHORT).show()
                return@Observer
            }


            //binding.textViewHourlyLoading.text = it.toString()
            binding.recyclerViewHourly.layoutManager = linearLayoutManager

            val hourlyWeatherMutableList:MutableList<Hourly> = it.hourly.toMutableList()

            hourlyWeatherMutableList.removeFirst()


            hourlyAdapter = HourlyWeatherAdapter(hourlyWeatherMutableList)
            binding.recyclerViewHourly.adapter = hourlyAdapter
            binding.textViewHourlyForecast.visibility = View.VISIBLE
        })

    }



    private fun setWeatherImage(id:Int)
    {
        when(id){

            in 200..232 -> binding.imageViewWeatherType.setImageResource(R.drawable.ic_storm)
            in 300..531 -> binding.imageViewWeatherType.setImageResource(R.drawable.ic_rain)
            in 600..622 -> binding.imageViewWeatherType.setImageResource(R.drawable.ic_snow)
            in 700..750 -> binding.imageViewWeatherType.setImageResource(R.drawable.ic_fog)
            800 -> binding.imageViewWeatherType.setImageResource(R.drawable.ic_clear_sun)
            801 -> binding.imageViewWeatherType.setImageResource(R.drawable.ic_sunny_few_clouds)
            in 802..804 -> binding.imageViewWeatherType.setImageResource(R.drawable.ic_cloudy)

        }

    }

    private fun showHumidityImage()
    {
        binding.imageViewHumidity.setImageResource(R.drawable.ic_singledrop)
    }

    private fun showCloudinessImage()
    {
        binding.imageViewCloudiness.setImageResource(R.drawable.ic_cloud)
    }

    private fun showWindImage()
    {
        binding.imageViewWind.setImageResource(R.drawable.ic_wind)
    }

    private fun setTemperature(temp: Double){
        binding.textViewTemperature.text = String.format(getString(R.string.temperature_place_holder),
            temp.roundToInt())
    }

    private fun setHumidity(humidityValue:Int)
    {

        binding.textViewHumidity.text = String.format(getString(R.string.humidity_place_holder),
            humidityValue)

    }

    private fun setCloudiness(cloudiness: Int){
        binding.textViewCloudiness.text = String.format(getString(R.string.cloudiness_place_holder),
            cloudiness)
    }
    private fun setWindSpeed(windSpeed:Double,units:String)
    {
        if(units == "METRIC") {
            binding.textViewWindSpeed.text = String.format(
                getString(R.string.wind_speed_place_holder_metric),
                windSpeed.roundToInt()
            )
        }
        else{
            binding.textViewWindSpeed.text = String.format(
                getString(R.string.wind_speed_place_holder_imperial),
                windSpeed.roundToInt()
            )
        }
    }






    private fun setWeatherDescription(id:Int)
    {
        when(id){

            //TODO ADD STRINGS DESCRIBING WEATHER BASED ON WEATHER ID AND SET IT

        }

        binding.textViewWeatherdescription.text  = "Cloudy" //TODO HARD CODED

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}