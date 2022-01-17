package com.example.weatherapp.ui.weather.weekly

import android.annotation.SuppressLint

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.data.db.entity.Daily
import com.example.weatherapp.data.provider.UnitProviderImpl
import com.example.weatherapp.databinding.DailyWeatherItemBinding
import com.example.weatherapp.databinding.FragmentDailyWeatherListBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class DailyListWeatherAdapter(private var dailyWeatherList: MutableList<Daily>): RecyclerView.Adapter<DailyListWeatherAdapter.DailyListWeatherAdapterViewHolder>() {



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DailyListWeatherAdapterViewHolder {


        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val shouldAttachToParentImmediately = false

        val view = DailyWeatherItemBinding.inflate(inflater,parent,shouldAttachToParentImmediately)


        return DailyListWeatherAdapterViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: DailyListWeatherAdapterViewHolder,
        position: Int
    ) {
        val item = dailyWeatherList[position]
        holder.bindDailyListWeather(item)

    }

    override fun getItemCount(): Int {
        return dailyWeatherList.size
    }

    class DailyListWeatherAdapterViewHolder(private val binding: DailyWeatherItemBinding): RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        var unitProvider: UnitProviderImpl = UnitProviderImpl(binding.root.context)
        init{
            binding.constraintLayoutDailyItem.setOnClickListener(this)
        }
        @SuppressLint("SimpleDateFormat")
        fun bindDailyListWeather(dailyWeather:Daily)
        {
            val isExpanded = dailyWeather.expanded
            if(!isExpanded) {

                binding.expandableLayoutDaily.visibility = View.GONE
            }
            val simpleDateFormat = SimpleDateFormat("EEE, d MMM",Locale.ENGLISH)

            val date = Date(dailyWeather.dt*1000L)


            binding.textViewTemperatureWeekly.text = dailyWeather.temp.day.roundToInt().toString() + "°"  //TODO ADD PLACE HOLDER
            binding.textViewWeeklyHumidity.text = dailyWeather.humidity.toString() + "%"  //TODO ADD PLACE HOLDER
            binding.textViewWeeklyCloudiness.text = dailyWeather.clouds.toString() + "%" //TODO ADD PLACE HOLDER
            binding.textViewFeelsLike.text = dailyWeather.feels_like.day.roundToInt().toString() + "°"
            binding.textViewPressure.text = dailyWeather.pressure.toString() + " hPa"
            binding.textViewDailyPrecipitation.text = dailyWeather.rain.toString() + " mm"
            binding.textViewUvIndex.text = dailyWeather.uvi.toString() //TODO Create fucntion for setting this


            setWindSpeed(dailyWeather.windSpeed,unitProvider.getUnitType().toString())

            binding.textViewWeeklyDescription.text = "xxx" // TODO CREATE FUNCTION FOR SETTING THIS

            binding.weeklyWeatherDate.text = simpleDateFormat.format(date)
            binding.ImageViewHumidity.setImageResource(R.drawable.ic_singledrop)
            binding.imageViewWindDaily.setImageResource(R.drawable.ic_wind)
            binding.imageViewCloudinessDaily.setImageResource(R.drawable.ic_cloud)
            setWeatherImage(dailyWeather.weather[0].id)


        }

        override fun onClick(v:View?) {

            if(binding.expandableLayoutDaily.visibility==View.GONE)
            {
                binding.expandableLayoutDaily.visibility = View.VISIBLE
                binding.imageViewArrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
            }

            else
            {
                binding.expandableLayoutDaily.visibility = View.GONE
                binding.imageViewArrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }


        private fun setWindSpeed(windSpeed:Double,units:String)
        {
            if(units == "METRIC") {
                binding.textViewWeeklyWind.text =  String.format(
                    binding.root.context.getString(R.string.wind_speed_place_holder_metric),
                    windSpeed.roundToInt()
                )
            }
            else{
                binding.textViewWeeklyWind.text = String.format(
                    binding.root.context.getString(R.string.wind_speed_place_holder_imperial),
                    windSpeed.roundToInt()
                )
            }
        }

        private fun setWeatherImage(id:Int)
        {
            when(id){

                in 200..232 -> binding.weeklyWeatherImage.setImageResource(R.drawable.ic_storm)
                in 300..531 -> binding.weeklyWeatherImage.setImageResource(R.drawable.ic_rain)
                in 600..622 -> binding.weeklyWeatherImage.setImageResource(R.drawable.ic_snow)
                in 700..750 -> binding.weeklyWeatherImage.setImageResource(R.drawable.ic_fog)
                800 -> binding.weeklyWeatherImage.setImageResource(R.drawable.ic_clear_sun)
                801 -> binding.weeklyWeatherImage.setImageResource(R.drawable.ic_sunny_few_clouds)
                in 802..804 -> binding.weeklyWeatherImage.setImageResource(R.drawable.ic_cloudy)

            }

        }

    }
}