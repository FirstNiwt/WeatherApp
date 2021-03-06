package com.example.weatherapp.ui.alerts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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


            if(binding.expandableLayout.visibility == View.GONE && binding.textViewDescription.text != "")
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


            binding.expandableLayout.visibility = View.GONE

            bindStartAndEndDate(alert.start,alert.end)
            bindDescriptions(alert.event,alert.description)

        }


        private fun bindStartAndEndDate(start:Int,end:Int)
        {
            val simpleDateFormat = SimpleDateFormat("EEE, HH:mm",Locale.getDefault())

            val timeStart = Date(start*1000L)
            val timeEnd = Date(end*1000L)

            binding.textViewAlertStartTime.text = simpleDateFormat.format(timeStart)
            binding.textViewAlertEndTime.text = simpleDateFormat.format(timeEnd)

        }

        private fun bindDescriptions(shortDesc:String, longDesc:String)
        {
            binding.textViewEventShortDesc.text = shortDesc.replaceFirstChar{it.uppercase()}
            binding.textViewDescription.text = longDesc
        }

    }


}



