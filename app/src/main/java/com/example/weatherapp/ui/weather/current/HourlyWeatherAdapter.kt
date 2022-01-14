package com.example.weatherapp.ui.weather.current

import android.annotation.SuppressLint
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.data.db.entity.Hourly
import com.example.weatherapp.databinding.HourlyWeatherItemBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class HourlyWeatherAdapter(private var hourlyWeatherList: MutableList<Hourly>): RecyclerView.Adapter<HourlyWeatherAdapter.HourlyWeatherViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyWeatherViewHolder {

        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val shouldAttachToParentImmediately = false

        val view = HourlyWeatherItemBinding.inflate(inflater,parent,shouldAttachToParentImmediately)
        return HourlyWeatherViewHolder(view)

    }

    override fun onBindViewHolder(holder: HourlyWeatherViewHolder, position: Int) {
        val item = hourlyWeatherList[position]

        holder.bindHourlyWeather(item)

    }

    override fun getItemCount(): Int {
        return hourlyWeatherList.size
    }

    class HourlyWeatherViewHolder(private val binding:HourlyWeatherItemBinding): RecyclerView.ViewHolder(binding.root) {


       fun bindHourlyWeather(hourlyWeather:Hourly)
        {

            val simpleDateFormat = SimpleDateFormat("HH:mm")

            val date = Date(hourlyWeather.dt*1000L)

            binding.hourlyWeatherTime.text = simpleDateFormat.format(date)
            setWeatherImage(hourlyWeather.weather[0].id)
            binding.hourlyWeatherTemperature.text = hourlyWeather.temp.roundToInt().toString() + "°" //TODO ADD PLACE HOLDER

            binding.textViewHumidityHourly.text = hourlyWeather.humidity.toString() + "%"  //TODO ADD PLACE HOLDER

        }


        private fun setWeatherImage(id:Int)
        {
            when(id){

                in 200..232 -> binding.hourlyWeatherImage.setImageResource(R.drawable.ic_storm)
                in 300..531 -> binding.hourlyWeatherImage.setImageResource(R.drawable.ic_rain)
                in 600..622 -> binding.hourlyWeatherImage.setImageResource(R.drawable.ic_snow)
                in 700..750 -> binding.hourlyWeatherImage.setImageResource(R.drawable.ic_fog)
                800 -> binding.hourlyWeatherImage.setImageResource(R.drawable.ic_clear_sun)
                801 -> binding.hourlyWeatherImage.setImageResource(R.drawable.ic_sunny_few_clouds)
                in 802..804 -> binding.hourlyWeatherImage.setImageResource(R.drawable.ic_cloudy)

            }

        }




    }
}