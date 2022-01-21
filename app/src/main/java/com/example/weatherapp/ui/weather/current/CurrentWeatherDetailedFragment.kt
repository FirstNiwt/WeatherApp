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
import com.example.weatherapp.data.provider.UnitProvider
import com.example.weatherapp.databinding.CurrentWeatherDetailedFragmentBinding
import com.example.weatherapp.ui.base.ScopedFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt



@AndroidEntryPoint
class CurrentWeatherDetailedFragment : ScopedFragment() {
    @Inject lateinit var viewModelFactory:CurrentWeatherViewModelFactory
    @Inject lateinit var unitProvider: UnitProvider
    private lateinit var hourlyAdapter: HourlyWeatherAdapter

    private var _binding: CurrentWeatherDetailedFragmentBinding? = null
    private val binding get() = _binding!!
    private var linearLayoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)



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

            val date = Date(System.currentTimeMillis() + (it.timezone - 3600) * 1000L)
            val sunset = (Date(it.sys.sunset * 1000L)).toInstant()
            val sunrise = (Date(it.sys.sunrise * 1000L)).toInstant()
            val sunsetDate = Date((it.sys.sunset + it.timezone -3600) * 1000L)
            val sunriseDate = Date((it.sys.sunrise + it.timezone -3600) * 1000L)

            val dateInstant = date.toInstant()

            val isNight:Boolean = dateInstant.isAfter(sunset) || dateInstant.isBefore(sunrise)


            val simpleDateFormat = SimpleDateFormat("EEE, dd MMM yyyy,  HH:mm", Locale.getDefault())
            val simpleTimeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

            val sunsetTime = simpleTimeFormat.format(sunsetDate)
            val sunriseTime = simpleTimeFormat.format(sunriseDate)

            (activity as? AppCompatActivity)?.supportActionBar?.subtitle = simpleDateFormat.format(date)
            (activity as? AppCompatActivity)?.supportActionBar?.title = it.name
            (activity as? AppCompatActivity)?.supportActionBar?.show()

            binding.groupLoadingCurrent.visibility = View.GONE


            setWeatherDescription(it.weather[0].description)
            setWeatherImage(it.weather[0].id,isNight)
            setTemperature(it.main.temp)

            showHumidityImage()
            setHumidity(it.main.humidity)

            showCloudinessImage()
            setCloudiness(it.clouds.all)


            showWindImage()
            setWindSpeed(it.wind.speed,unitProvider.getUnitType().toString())

            binding.groupSmallInfo.visibility = View.VISIBLE
            binding.textViewSunriseTime.text = sunriseTime
            binding.textViewSunsetTime.text = sunsetTime


            })

        futureWeather.observe(this@CurrentWeatherDetailedFragment, Observer{
            if(it == null)
            {
                Toast.makeText(context,"Not initialized",Toast.LENGTH_SHORT).show()
                return@Observer
            }



            binding.recyclerViewHourly.layoutManager = linearLayoutManager

            val hourlyWeatherMutableList:MutableList<Hourly> = it.hourly.toMutableList()

            hourlyWeatherMutableList.removeFirst()
            val hourlyTrim = hourlyWeatherMutableList.take(8).toMutableList()

            hourlyAdapter = HourlyWeatherAdapter(hourlyTrim,it.daily[0].sunrise,it.daily[0].sunset,
                it.timezoneOffset)
            binding.recyclerViewHourly.adapter = hourlyAdapter
            binding.textViewHourlyForecast.visibility = View.VISIBLE
        })

    }



    private fun setWeatherImage(id:Int,isNight:Boolean)
    {
        if(!isNight)
        when(id){

            in 200..232 -> binding.imageViewWeatherType.setImageResource(R.drawable.ic_storm)
            in 300..531 -> binding.imageViewWeatherType.setImageResource(R.drawable.ic_rain)
            in 600..622 -> binding.imageViewWeatherType.setImageResource(R.drawable.ic_snow)
            in 700..750 -> binding.imageViewWeatherType.setImageResource(R.drawable.ic_fog)
            800 -> binding.imageViewWeatherType.setImageResource(R.drawable.ic_clear_sun)
            801 -> binding.imageViewWeatherType.setImageResource(R.drawable.ic_sunny_few_clouds)
            in 802..804 -> binding.imageViewWeatherType.setImageResource(R.drawable.ic_cloudy)

        }

        else{
            when(id) {
                in 200..232 -> binding.imageViewWeatherType.setImageResource(R.drawable.ic_storm_at_night)
                in 300..531 -> binding.imageViewWeatherType.setImageResource(R.drawable.ic_night_with_rain)
                in 600..622 -> binding.imageViewWeatherType.setImageResource(R.drawable.ic_storm_at_night)
                in 700..750 -> binding.imageViewWeatherType.setImageResource(R.drawable.ic_fog)
                800 -> binding.imageViewWeatherType.setImageResource(R.drawable.ic_moon)
                801 -> binding.imageViewWeatherType.setImageResource(R.drawable.ic_moon)
                in 802..804 -> binding.imageViewWeatherType.setImageResource(R.drawable.ic_moon)
            }
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




    private fun setWeatherDescription(description:String)
    {

        binding.textViewWeatherdescription.text  = description.replaceFirstChar{it.uppercase()}

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}