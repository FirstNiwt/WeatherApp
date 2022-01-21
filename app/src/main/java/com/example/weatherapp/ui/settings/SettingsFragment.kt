package com.example.weatherapp.ui.settings

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.weatherapp.R

class SettingsFragment: PreferenceFragmentCompat() {

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            addPreferencesFromResource(R.xml.preferences)
        }

    fun registerPref(context: Context,listener: OnSharedPreferenceChangeListener){
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        pref.registerOnSharedPreferenceChangeListener(listener)
    }

    fun unregisterPref(context: Context,listener: OnSharedPreferenceChangeListener){
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        pref.unregisterOnSharedPreferenceChangeListener(listener)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Settings"
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = null
    }


}