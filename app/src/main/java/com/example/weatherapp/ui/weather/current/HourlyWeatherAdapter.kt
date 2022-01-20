package com.example.weatherapp.ui.weather.current


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.data.db.entity.Hourly
import com.example.weatherapp.databinding.HourlyWeatherItemBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class HourlyWeatherAdapter(private var hourlyWeatherList: MutableList<Hourly>,private val sunriseTime:Int
,private val sunsetTime:Int , private val timezoneOffset:Int):
    RecyclerView.Adapter<HourlyWeatherAdapter.HourlyWeatherViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyWeatherViewHolder {

        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val shouldAttachToParentImmediately = false

        val view = HourlyWeatherItemBinding.inflate(inflater,parent,shouldAttachToParentImmediately)
        return HourlyWeatherViewHolder(view)

    }

    override fun onBindViewHolder(holder: HourlyWeatherViewHolder, position: Int) {
        val item = hourlyWeatherList[position]
        item.sunrise=sunriseTime
        item.sunset=sunsetTime
        item.timezone =timezoneOffset
        holder.bindHourlyWeather(item)

    }

    override fun getItemCount(): Int {
        return hourlyWeatherList.size
    }

    class HourlyWeatherViewHolder(private val binding:HourlyWeatherItemBinding): RecyclerView.ViewHolder(binding.root) {


       @SuppressLint("SimpleDateFormat")
       fun bindHourlyWeather(hourlyWeather:Hourly)
        {
            val date = Date((hourlyWeather.dt + hourlyWeather.timezone -3600) *1000L)
            val sunset = (Date((hourlyWeather.sunset + hourlyWeather.timezone-3600) * 1000L)).toInstant()
            val sunrise = (Date((hourlyWeather.sunrise + hourlyWeather.timezone-3600) * 1000L)).toInstant()

            val dateInstant = date.toInstant()

            val isNight:Boolean = dateInstant.isAfter(sunset) || dateInstant.isBefore(sunrise)

            val simpleDateFormat = SimpleDateFormat("HH:mm")

            binding.hourlyWeatherTime.text = simpleDateFormat.format(date)

            setWeatherImage(hourlyWeather.weather[0].id, isNight)
            setTemperature(hourlyWeather.temp)
            setHumidity(hourlyWeather.humidity)

        }


        private fun setWeatherImage(id:Int,isNight:Boolean)
        {
            if(!isNight)
                when(id){

                    in 200..232 -> binding.hourlyWeatherImage.setImageResource(R.drawable.ic_storm)
                    in 300..531 -> binding.hourlyWeatherImage.setImageResource(R.drawable.ic_rain)
                    in 600..622 -> binding.hourlyWeatherImage.setImageResource(R.drawable.ic_snow)
                    in 700..750 -> binding.hourlyWeatherImage.setImageResource(R.drawable.ic_fog)
                    800 -> binding.hourlyWeatherImage.setImageResource(R.drawable.ic_clear_sun)
                    801 -> binding.hourlyWeatherImage.setImageResource(R.drawable.ic_sunny_few_clouds)
                    in 802..804 -> binding.hourlyWeatherImage.setImageResource(R.drawable.ic_cloudy)

                }

            else{
                when(id) {
                    in 200..232 -> binding.hourlyWeatherImage.setImageResource(R.drawable.ic_storm_at_night)
                    in 300..531 -> binding.hourlyWeatherImage.setImageResource(R.drawable.ic_night_with_rain)
                    in 600..622 -> binding.hourlyWeatherImage.setImageResource(R.drawable.ic_storm_at_night)
                    in 700..750 -> binding.hourlyWeatherImage.setImageResource(R.drawable.ic_fog)
                    800 -> binding.hourlyWeatherImage.setImageResource(R.drawable.ic_moon)
                    801 -> binding.hourlyWeatherImage.setImageResource(R.drawable.ic_moon)
                    in 802..804 -> binding.hourlyWeatherImage.setImageResource(R.drawable.ic_moon)
                }
            }
        }


        private fun setTemperature(temp:Double)
        {
            binding.hourlyWeatherTemperature.text  = String.format(binding.root.context
                .getString(R.string.temperature_place_holder)
                ,temp.roundToInt())
        }

        private fun setHumidity(humidityValue:Int)
        {

            binding.textViewHumidityHourly.text = String.format(binding.root.context
                .getString(R.string.humidity_place_holder),
                humidityValue)

        }
    }

}