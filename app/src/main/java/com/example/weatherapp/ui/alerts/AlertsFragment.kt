package com.example.weatherapp.ui.alerts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.data.db.entity.Alert
import com.example.weatherapp.data.provider.LocationProvider
import com.example.weatherapp.databinding.FragmentAlertsBinding
import com.example.weatherapp.ui.base.ScopedFragment
import com.example.weatherapp.ui.weather.weekly.DailyWeatherListFragment
import com.example.weatherapp.ui.weather.weekly.DailyWeatherListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AlertsFragment : ScopedFragment() {
    @Inject
    lateinit var viewModelFactory: AlertsViewModelFactory
    @Inject
    lateinit var locationProvider: LocationProvider

    private lateinit var alertsAdapter: AlertsAdapter


    private var _binding: FragmentAlertsBinding? = null
    private val binding get() = _binding!!
    private var linearLayoutManager =
        LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = null
        (activity as? AppCompatActivity)?.supportActionBar?.title = null
        super.onCreate(savedInstanceState)
    }

    companion object {
        fun newInstance() = AlertsFragment()
    }

    private lateinit var viewModel: AlertsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAlertsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[AlertsViewModel::class.java]

        bindUI()

    }

    private fun provideLanguage():Locale{

        return if(locationProvider.getLanguage() == "ENGLISH")
            Locale.ENGLISH
        else
            Locale.getDefault()

    }

    private fun bindUI() = launch {

        var cityName = " "

        val currentWeather = viewModel.currentWeather.await()
        val futureWeather = viewModel.futureWeather.await()



        currentWeather.observe(this@AlertsFragment, Observer {
            if (it == null) {
                Toast.makeText(context, "Not initialized", Toast.LENGTH_SHORT).show()
                return@Observer


            }

            cityName = it.name
        })


        futureWeather.observe(this@AlertsFragment, Observer {
            if (it == null) {
                Toast.makeText(context, "Not initialized", Toast.LENGTH_SHORT).show()
                return@Observer
            }
            val simpleDateFormat = SimpleDateFormat("EEE, dd MMM yyyy,  HH:mm",Locale.getDefault())
            val date = Date(System.currentTimeMillis() + (it.timezoneOffset - 3600) * 1000L)

            (activity as? AppCompatActivity)?.supportActionBar?.subtitle = simpleDateFormat.format(date)
            (activity as? AppCompatActivity)?.supportActionBar?.title = cityName
            (activity as? AppCompatActivity)?.supportActionBar?.show()
            binding.mainRecyclerView.layoutManager = linearLayoutManager


            val alertsMutableList:MutableList<Alert> = it.alerts.toMutableList()

            if(alertsMutableList.size>1 || (alertsMutableList.size==1 && alertsMutableList[0].event!=""))
            {

                alertsAdapter = AlertsAdapter(alertsMutableList)
                binding.mainRecyclerView.adapter = alertsAdapter

                binding.groupLoadingAlerts.visibility = View.GONE
            }

            else {
                binding.groupLoadingAlerts.visibility = View.GONE
                binding.textViewNoAlerts.visibility = View.VISIBLE

            }


        })


    }
}