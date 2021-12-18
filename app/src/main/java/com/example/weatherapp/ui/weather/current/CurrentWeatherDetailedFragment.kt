package com.example.weatherapp.ui.weather.current

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.weatherapp.R
import com.example.weatherapp.data.network.OpenWeatherApiService
import com.example.weatherapp.data.network.WeatherNetworkDataSourceImpl
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

    }

    private fun bindUI() = launch{

        val currentWeather = viewModel.currentWeather.await()
        val futureWeather = viewModel.futureWeather.await()

        currentWeather.observe(this@CurrentWeatherDetailedFragment, Observer {
            if(it == null) {
                Toast.makeText(context,"Not initialized",Toast.LENGTH_SHORT).show()
                return@Observer


            }

            binding.progressBar.visibility = View.GONE    //TODO CREATE VIEW GROUP FOR THIS
            binding.textViewLoading.visibility = View.GONE



            showLocation("Olkusz")
            showDate()

            setWeatherImage(it.weather[0].id)
            setTemperature(it.main.temp)

            showHumidityImage()
            setHumidity(it.main.humidity)

            showCloudinessImage()
            setCloudiness(it.clouds.all)


            showWindImage()
            setWindSpeed(it.wind.speed,"") //TODO get units from settings

            showHourlyForecastText()

            binding.progressBarHourlyloading.visibility = View.VISIBLE
            binding.textViewHourlyLoading.text = "Wczytywanie Danych" //TODO HARD CODED
            binding.textViewHourlyLoading.visibility = View.VISIBLE


            })

        futureWeather.observe(this@CurrentWeatherDetailedFragment, Observer{
            if(it == null)
            {
                Toast.makeText(context,"Not initialized",Toast.LENGTH_SHORT).show()
                return@Observer
            }
            Log.e("X",it.timezone)
            binding.progressBarHourlyloading.visibility = View.GONE  //TODO CREATE VIEW GROUP FOR THIS
            //binding.textViewHourlyLoading.visibility = View.GONE
            binding.textViewHourlyLoading.text = it.toString()


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
        binding.imageViewHumidity.setImageResource(R.drawable.ic_humidity)
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
        binding.textViewWindSpeed.text = String.format(getString(R.string.wind_speed_place_holder_metric),
            windSpeed.roundToInt())

    }

    private fun showHourlyForecastText()
    {
        binding.textViewHourlyForecast.text = "PROGNOZA GODZINOWA"//TODO make it based on language selected in settings
    }

    private fun showLocation(location: String){
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
        (activity as? AppCompatActivity)?.supportActionBar?.show()
    }

    private fun showDate(){
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "piątek, 17 grudnia 2021" //TODO Set it based on current date

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}