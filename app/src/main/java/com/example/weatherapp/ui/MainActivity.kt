package com.example.weatherapp.ui

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
import android.content.res.Resources
import android.os.Handler
import android.util.AttributeSet
import android.view.*

import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.load.engine.Resource
import com.example.weatherapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.checkerframework.checker.units.qual.m


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setSupportActionBar(binding.topMenu)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment

        val navView:BottomNavigationView = binding.bottomNav
        navView.setBackgroundColor(ResourcesCompat.getColor(resources,R.color.colorPrimaryDark,theme))



        navController = navHostFragment.navController

        binding.bottomNav.setupWithNavController(navController)

        NavigationUI.setupActionBarWithNavController(this,navController)




    }


    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (menu is MenuBuilder) {
            menu.setOptionalIconsVisible(true)
        }

        menuInflater.inflate(com.example.weatherapp.R.menu.top_app_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController,null)
    }




}