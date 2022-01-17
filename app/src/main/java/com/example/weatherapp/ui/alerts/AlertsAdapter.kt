package com.example.weatherapp.ui.alerts

import android.telecom.Call
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.data.db.entity.Alert
import com.example.weatherapp.data.provider.UnitProviderImpl
import com.example.weatherapp.databinding.AlertItemBinding
import java.text.SimpleDateFormat
import java.util.*

class AlertsAdapter(private var alertsList: MutableList<Alert>): RecyclerView.Adapter<AlertsAdapter.AlertsAdapterViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertsAdapterViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val shouldAttachToParentImmediately = false

        val view = AlertItemBinding.inflate(inflater,parent,shouldAttachToParentImmediately)
        return AlertsAdapterViewHolder(view)

    }

    override fun onBindViewHolder(holder: AlertsAdapterViewHolder, position: Int) {
        val item= alertsList[position]
        holder.bindAlertItem(item)

    }



    override fun getItemCount(): Int {
        return alertsList.size
    }


    class AlertsAdapterViewHolder(private val binding:AlertItemBinding):RecyclerView.ViewHolder(binding.root),View.OnClickListener {
        var unitProvider: UnitProviderImpl = UnitProviderImpl(binding.root.context)

        init{

            binding.constraintLayoutMain.setOnClickListener(this)
        }

        override fun onClick(v:View?) {

            if(binding.expandableLayout.visibility == View.GONE)
            {
                binding.expandableLayout.visibility = View.VISIBLE

            }

            else
            {
                binding.expandableLayout.visibility = View.GONE

            }
        }


        fun bindAlertItem(alert:Alert)
        {


            val isExpanded = alert.expanded
            if(!isExpanded) {

                binding.expandableLayout.visibility = View.GONE
            }
            val simpleDateFormat = SimpleDateFormat("EEE, HH:mm",Locale.ENGLISH)




            val timeStart = Date(alert.start*1000L)
            val timeEnd = Date(alert.end*1000L)

            binding.textViewAlertStartTime.text = simpleDateFormat.format(timeStart)
            binding.textViewAlertEndTime.text = simpleDateFormat.format(timeEnd)

            binding.textViewEventShortDesc.text = alert.event.replaceFirstChar{it.uppercase()}
            binding.textViewDescription.text = alert.description




        }


    }


}



