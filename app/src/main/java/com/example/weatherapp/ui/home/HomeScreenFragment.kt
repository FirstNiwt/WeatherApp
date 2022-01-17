package com.example.weatherapp.ui.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.data.provider.UnitProvider
import com.example.weatherapp.databinding.FragmentHomeScreenBinding
import com.example.weatherapp.ui.base.ScopedFragment
import com.example.weatherapp.ui.settings.SettingsFragment
import com.example.weatherapp.ui.weather.current.CurrentWeatherDetailedViewModel
import com.example.weatherapp.ui.weather.current.CurrentWeatherViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt
import android.R
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController


@AndroidEntryPoint
class HomeScreenFragment : ScopedFragment(){
    @Inject
    lateinit var viewModelFactory: HomeScreenViewModelFactory
    @Inject
    lateinit var unitProvider: UnitProvider



    private var _binding: FragmentHomeScreenBinding? = null
    private val binding get() = _binding!!


    private lateinit var viewModel: HomeScreenViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.constraintLayoutMain.setOnClickListener {
            val action = HomeScreenFragmentDirections.actionHomeScreenFragmentToCurrentWeatherDetailedFragment()
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


        currentWeather.observe(this@HomeScreenFragment, Observer{

            if (it == null) {
                Toast.makeText(context, "Not initialized", Toast.LENGTH_SHORT).show()
                return@Observer

            }

            val simpleDateFormat = SimpleDateFormat("EEE, dd MMM yyyy", Locale.ENGLISH)
            val simpleTimeFormat = SimpleDateFormat("HH:mm",Locale.ENGLISH)
            val date = Date(it.dt*1000L)
            val time = Date((it.dt + it.timezone - 3600)*1000L)

            (activity as? AppCompatActivity)?.supportActionBar?.subtitle = simpleDateFormat.format(date)
            (activity as? AppCompatActivity)?.supportActionBar?.title = it.name
            (activity as? AppCompatActivity)?.supportActionBar?.show()


            binding.textViewHomeTemp.text = it.main.temp.roundToInt().toString() + "°"
            binding.textViewHomeTempMax.text = it.main.tempMax.roundToInt().toString() +"°"
            binding.textViewHomeTempMin.text = it.main.tempMin.roundToInt().toString() + "°"
            binding.textViewHomeUpdateTime.text = "Updated at "+ simpleTimeFormat.format(time)
            binding.imageView2.visibility = View.VISIBLE
            binding.imageView5.visibility = View.VISIBLE
            binding.textViewHomeDescription.text = "Partly Cloudy" //TODO add setter for this

            //TODO ADD LOADING FOR THIS


        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}