package com.example.weatherapp.ui

import android.Manifest
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.ui.home.HomeScreenFragment
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import java.lang.RuntimeException
import java.lang.reflect.Method
import android.R.menu
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Handler
import android.util.AttributeSet
import android.view.*
import android.widget.Toast

import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.load.engine.Resource
import com.example.weatherapp.R
import com.example.weatherapp.data.network.WeatherNetworkDataSource
import com.example.weatherapp.data.repository.ForecastRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.checkerframework.checker.units.qual.m
import javax.inject.Inject

private const val MY_PERMISSION_ACCESS_COARSE_LOCATION = 1

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    private val locationCallback = object: LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setSupportActionBar(binding.topMenu)
        setContentView(binding.root)


        val navHostFragment = supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment

        val navView:BottomNavigationView = binding.bottomNav
        navView.setBackgroundColor(ResourcesCompat.getColor(resources,R.color.light_sea_blue,theme))



        navController = navHostFragment.navController

        binding.bottomNav.setupWithNavController(navController)

        NavigationUI.setupActionBarWithNavController(this,navController)

        requestLocationPermission()

        if(hasLocationPermission()){
            bindLocationManager()
        }
        else
            requestLocationPermission()
    }


    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (menu is MenuBuilder) {
            menu.setOptionalIconsVisible(true)
        }

        menuInflater.inflate(R.menu.top_app_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController,null)
    }

    private fun requestLocationPermission(){
        ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION)
            ,MY_PERMISSION_ACCESS_COARSE_LOCATION)
    }

    private fun bindLocationManager(){

        LifecycleBoundLocationManager(this,fusedLocationProviderClient,locationCallback)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == MY_PERMISSION_ACCESS_COARSE_LOCATION){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            bindLocationManager()
        }
        else
            Toast.makeText(this,"Please,set location manually in settings",Toast.LENGTH_LONG).show()
    }

    private fun hasLocationPermission():Boolean{
        return ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
}