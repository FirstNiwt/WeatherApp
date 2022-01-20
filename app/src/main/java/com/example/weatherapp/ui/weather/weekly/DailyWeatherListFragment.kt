package com.example.weatherapp.ui.weather.weekly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.db.entity.Daily
import com.example.weatherapp.databinding.FragmentDailyWeatherListBinding
import com.example.weatherapp.ui.base.ScopedFragment
import com.xwray.groupie.GroupAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class DailyWeatherListFragment : ScopedFragment() {
    @Inject lateinit var viewModelFactory: DailyWeatherListViewModelFactory
    private lateinit var dailyAdapter: DailyListWeatherAdapter



    private var _binding: FragmentDailyWeatherListBinding? = null
    private val binding get() = _binding!!
    private var linearLayoutManager = LinearLayoutManager(this@DailyWeatherListFragment.context,
        LinearLayoutManager.VERTICAL,false)

    companion object{
        fun newInstance() = DailyWeatherListFragment()
    }

    private lateinit var viewModel: DailyWeatherListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = null
        (activity as? AppCompatActivity)?.supportActionBar?.title = null
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDailyWeatherListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[DailyWeatherListViewModel::class.java]

        bindUI()

    }

    private fun bindUI() = launch{

        var cityName = " "

        val futureWeather = viewModel.futureWeather.await()
        val currentWeather = viewModel.currentWeather.await()


        currentWeather.observe(this@DailyWeatherListFragment, Observer {
            if (it == null) {
                Toast.makeText(context, "Not initialized", Toast.LENGTH_SHORT).show()
                return@Observer
            }

            cityName = it.name
            })



            futureWeather.observe(this@DailyWeatherListFragment, Observer{
            if(it == null)
            {
                Toast.makeText(context,"Not initialized", Toast.LENGTH_SHORT).show()
                return@Observer
            }


            val simpleDateFormat = SimpleDateFormat("EEE, dd MMM yyyy,  HH:mm", Locale.ENGLISH)
            val date = Date(System.currentTimeMillis() + (it.timezoneOffset - 3600) * 1000L)

            (activity as? AppCompatActivity)?.supportActionBar?.subtitle = simpleDateFormat.format(date)
            (activity as? AppCompatActivity)?.supportActionBar?.title = cityName
            (activity as? AppCompatActivity)?.supportActionBar?.show()


            val dailyWeatherMutableList:MutableList<Daily> = it.daily.toMutableList()
                dailyAdapter = DailyListWeatherAdapter(dailyWeatherMutableList)

                binding.mainRecyclerView.apply{
                    layoutManager = linearLayoutManager
                    adapter = dailyAdapter
                }


                    binding.groupLoadingDaily.visibility = View.GONE
                    binding.mainRecyclerView.visibility = View.VISIBLE



        })

    }
}