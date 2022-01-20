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

            val date = Date(dailyWeather.dt*1000L)


            setTemperature(dailyWeather.temp.day)
            setCloudiness(dailyWeather.clouds)
            setHumidity(dailyWeather.humidity)
            setFeelsLikeTemp(dailyWeather.feels_like.day)
            setPressure(dailyWeather.pressure)
            setPrecipitation(dailyWeather.rain,dailyWeather.snow)
            setUvIndex(dailyWeather.uvi.roundToInt())

            setWindSpeed(dailyWeather.windSpeed,unitProvider.getUnitType().toString())
            setDescription(dailyWeather.weather[0].description)
            setDate(date)

            setSmallWeatherImages()
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

        private fun setSmallWeatherImages()
        {
            binding.ImageViewHumidity.setImageResource(R.drawable.ic_singledrop)
            binding.imageViewWindDaily.setImageResource(R.drawable.ic_wind)
            binding.imageViewCloudinessDaily.setImageResource(R.drawable.ic_cloud)

        }
        private fun setDescription(description:String)
        {
            binding.textViewWeeklyDescription.text = description.replaceFirstChar{it.uppercase()}
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
        private fun setDate(date:Date){
            val simpleDateFormat = SimpleDateFormat("EEE, d MMM",Locale.ENGLISH)
            binding.weeklyWeatherDate.text = simpleDateFormat.format(date)
        }
        private fun setTemperature(temp: Double){
            binding.textViewTemperatureWeekly.text = String.format(binding.root.context
                .getString(R.string.temperature_place_holder), temp.roundToInt())
        }

        private fun setCloudiness(cloud: Int){
            binding.textViewWeeklyCloudiness.text = String.format(binding.root.context
                .getString(R.string.cloudiness_place_holder), cloud)
        }
        private fun setHumidity(humidityValue:Int)
        {
            binding.textViewWeeklyHumidity.text = String.format(binding.root.context.
            getString(R.string.humidity_place_holder), humidityValue)

        }

        private fun setFeelsLikeTemp(temp: Double){
            binding.textViewFeelsLike.text = String.format(binding.root.context
                .getString(R.string.temperature_place_holder), temp.roundToInt())
        }

        private fun setPressure(pressure:Int){
            binding.textViewPressure.text = String.format(binding.root.context
                .getString(R.string.pressure_place_holder),pressure)
        }

        private fun setPrecipitation(precipitationRain: Double,precipitationSnow:Double){
            if(precipitationRain>precipitationSnow)
                binding.textViewDailyPrecipitation.text = String.format(binding.root.context
                    .getString(R.string.precipitation_place_holder),precipitationRain)

            else {
                binding.textViewDailyPrecipitation.text = String.format(binding.root.context
                    .getString(R.string.precipitation_place_holder), precipitationSnow)
            }
        }
        private fun setUvIndex(uv:Int){

            when(uv)
            {
                in 0..2 -> binding.textViewUvIndex.text = binding.root.context.getString(R.string.uv_low)
                in 3..5 -> binding.textViewUvIndex.text = binding.root.context.getString(R.string.uv_moderate)
                in 6..7 -> binding.textViewUvIndex.text = binding.root.context.getString(R.string.uv_high)
                in 8..10 -> binding.textViewUvIndex.text = binding.root.context.getString(R.string.uv_very_high)
                in 11..20 -> binding.textViewUvIndex.text = binding.root.context.getString(R.string.uv_extreme)
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