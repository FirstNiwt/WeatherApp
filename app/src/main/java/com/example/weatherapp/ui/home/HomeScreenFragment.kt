package com.example.weatherapp.ui.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.data.provider.UnitProvider
import com.example.weatherapp.databinding.FragmentHomeScreenBinding
import com.example.weatherapp.ui.base.ScopedFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.R
import com.example.weatherapp.data.db.entity.Alert
import com.example.weatherapp.data.provider.LocationProvider


@AndroidEntryPoint
class HomeScreenFragment : ScopedFragment(){
    @Inject
    lateinit var viewModelFactory: HomeScreenViewModelFactory
    @Inject
    lateinit var unitProvider: UnitProvider
    @Inject
    lateinit var locationProvider:LocationProvider


    private var _binding: FragmentHomeScreenBinding? = null
    private val binding get() = _binding!!


    private lateinit var viewModel: HomeScreenViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.constraintLayoutMain.setOnClickListener {
            val action = HomeScreenFragmentDirections.actionHomeScreenFragmentToCurrentWeatherDetailedFragment()
            findNavController().navigate(action)
        }

        binding.textViewAlertHome.setOnClickListener {
            val action = HomeScreenFragmentDirections.actionHomeScreenFragmentToAlertsFragment()
            findNavController().navigate(action)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeScreenBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[HomeScreenViewModel::class.java]



        bindUI()

    }

    private fun bindUI() = launch {

        val currentWeather = viewModel.currentWeather.await()
        val futureWeather = viewModel.futureWeather.await()

        currentWeather.observe(this@HomeScreenFragment, Observer{

            if (it == null) {
                Toast.makeText(context, "Not initialized", Toast.LENGTH_SHORT).show()
                return@Observer

            }

            val simpleDateFormat = SimpleDateFormat("EEE, dd MMM yyyy,  HH:mm", Locale.getDefault())
            val simpleTimeFormat = SimpleDateFormat("HH:mm",Locale.getDefault())

            val date = Date(System.currentTimeMillis() + (it.timezone - 3600) * 1000L)
            val sunset = (Date(it.sys.sunset * 1000L)).toInstant()
            val sunrise = (Date(it.sys.sunrise * 1000L)).toInstant()

            val dateInstant = date.toInstant()

            val isNight:Boolean = dateInstant.isAfter(sunset) || dateInstant.isBefore(sunrise)





            (activity as? AppCompatActivity)?.supportActionBar?.subtitle = simpleDateFormat.format(date)
            (activity as? AppCompatActivity)?.supportActionBar?.title = it.name
            (activity as? AppCompatActivity)?.supportActionBar?.show()


            setMainTemperature(it.main.temp)
            setMaxTemperature(it.main.tempMax)
            setMinTemperature(it.main.tempMin)
            setUpdateTime(simpleTimeFormat.format(date))
            binding.imageViewArrowUp.visibility = View.VISIBLE
            binding.imageViewArrowDown.visibility = View.VISIBLE
            setDescription(it.weather[0].description)
            setMainScreenPhoto(it.weather[0].id,isNight)
            binding.groupLoadingHome.visibility = View.GONE
            binding.homeScreenAllVisible.visibility = View.VISIBLE

        })

        futureWeather.observe(this@HomeScreenFragment, Observer {

            if (it == null) {
                Toast.makeText(context, "Not initialized", Toast.LENGTH_SHORT).show()
                return@Observer

            }
            val alertsMutableList:MutableList<Alert> = it.alerts.toMutableList()
            val isHomeAlertEnabled:Boolean = locationProvider.isHomeAlert()

            if((alertsMutableList.size>1 || (alertsMutableList.size==1 && alertsMutableList[0].event!=""))&& isHomeAlertEnabled)
            {
                binding.homeScreenAlertGroup.visibility = View.VISIBLE

            }

            else{
                binding.homeScreenAlertGroup.visibility = View.GONE
            }

        })
    }

    private fun setDescription(description :String)
    {
        binding.textViewHomeDescription.text = description.replaceFirstChar{it.uppercase()}
    }

    private fun setMainTemperature(temp:Double)
    {
        binding.textViewHomeTemp.text = String.format(getString(R.string.temperature_place_holder)
            ,temp.roundToInt())
    }


    private fun setMaxTemperature(maxTemp:Double){

        binding.textViewHomeTempMax.text = String.format(getString(R.string.temperature_place_holder)
            ,maxTemp.roundToInt())
    }

    private fun setMinTemperature(minTemp:Double){

        binding.textViewHomeTempMin.text = String.format(getString(R.string.temperature_place_holder)
            ,minTemp.roundToInt())
    }

    private fun setUpdateTime(time:String){

        binding.textViewHomeUpdateTime.text = String.format(getString(R.string.update_time_place_holder),time)
    }

    private fun setMainScreenPhoto(id:Int, isNight:Boolean) {

        if (!isNight) {
            when (id) {

                in 200..232 -> binding.imageViewHomePhoto.setImageResource(R.drawable.storm)
                in 300..531 -> binding.imageViewHomePhoto.setImageResource(R.drawable.rain)
                in 600..622 -> binding.imageViewHomePhoto.setImageResource(R.drawable.snow)
                in 700..750 -> binding.imageViewHomePhoto.setImageResource(R.drawable.fog)
                800 -> binding.imageViewHomePhoto.setImageResource(R.drawable.sunny)
                801 -> binding.imageViewHomePhoto.setImageResource(R.drawable.clouds)
                in 802..804 -> binding.imageViewHomePhoto.setImageResource(R.drawable.dark_clouds)

            }
        } else {
            when (id) {
                in 200..232 -> binding.imageViewHomePhoto.setImageResource(R.drawable.night_storm)
                in 300..531 -> binding.imageViewHomePhoto.setImageResource(R.drawable.night_rain)
                in 600..622 -> binding.imageViewHomePhoto.setImageResource(R.drawable.night_snow)
                in 700..750 -> binding.imageViewHomePhoto.setImageResource(R.drawable.night_fog)
                in 800..804 -> binding.imageViewHomePhoto.setImageResource(R.drawable.night_clouds)

            }

        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}