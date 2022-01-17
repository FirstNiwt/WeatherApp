package com.example.weatherapp.di

import android.app.Application
import android.content.Context
import com.example.weatherapp.data.CurrentWeatherDao
import com.example.weatherapp.data.FutureWeatherDao
import com.example.weatherapp.data.db.ForecastDatabase
import com.example.weatherapp.data.network.*
import com.example.weatherapp.data.provider.LocationProvider
import com.example.weatherapp.data.provider.LocationProviderImpl
import com.example.weatherapp.data.provider.UnitProvider
import com.example.weatherapp.data.provider.UnitProviderImpl
import com.example.weatherapp.data.repository.ForecastRepository
import com.example.weatherapp.data.repository.ForecastRepositoryImpl
import com.example.weatherapp.ui.alerts.AlertsViewModel
import com.example.weatherapp.ui.alerts.AlertsViewModelFactory
import com.example.weatherapp.ui.home.HomeScreenViewModelFactory
import com.example.weatherapp.ui.weather.current.CurrentWeatherViewModelFactory
import com.example.weatherapp.ui.weather.weekly.DailyWeatherListViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideForecastDatabase(@ApplicationContext app: Context) = ForecastDatabase(app)

    @Provides
    @Singleton
    fun provideCurrentWeatherDao(db: ForecastDatabase) = db.getCurrentWeatherDao()


    @Provides
    @Singleton
    fun provideFutureWeatherDao(db: ForecastDatabase) = db.getFutureWeatherDao()

    @Provides
    @Singleton
    fun providesWeatherApiService(connectivityInterceptor:
    ConnectivityInterceptor) = OpenWeatherApiService(connectivityInterceptor)

    @Provides
    @Singleton
    fun provideConnectivityInterceptorImpl(@ApplicationContext app: Context) = ConnectivityInterceptorImpl(app)


    @Provides
    @Singleton
    fun provideForecastRepositoryImpl(currentWeatherDao: CurrentWeatherDao,futureWeatherDao:FutureWeatherDao,
        weatherNetworkDataSource: WeatherNetworkDataSource,locationProvider: LocationProvider) =
        ForecastRepositoryImpl(currentWeatherDao,futureWeatherDao, weatherNetworkDataSource,locationProvider)

    @Provides
    @Singleton
    fun provideWeatherNetworkDataSourceImpl(openWeatherApiService: OpenWeatherApiService)
    = WeatherNetworkDataSourceImpl(openWeatherApiService)

    @Provides
    @Singleton
    fun provideCurrentWeatherViewModelFactory(forecastRepository: ForecastRepository,unitProvider: UnitProvider)
    = CurrentWeatherViewModelFactory(forecastRepository,unitProvider)

    @Provides
    @Singleton
    fun provideDailyWeatherListViewModelFactory(forecastRepository: ForecastRepository, unitProvider:UnitProvider)
            = DailyWeatherListViewModelFactory(forecastRepository,unitProvider)

    @Provides
    @Singleton
    fun provideUnitProviderImpl(@ApplicationContext app: Context) = UnitProviderImpl(app)

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(baseApplication: Application): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(baseApplication)

    @Provides
    fun provideLocationProviderImpl(fusedLocationProviderClient: FusedLocationProviderClient,
       @ApplicationContext app :Context) = LocationProviderImpl(fusedLocationProviderClient,app)

    @Provides
    @Singleton
    fun provideAlertsViewModelFactory(forecastRepository: ForecastRepository,unitProvider: UnitProvider)=
        AlertsViewModelFactory(forecastRepository,unitProvider)

    @Provides
    @Singleton
    fun provideHomeScreenViewModelFactory(forecastRepository: ForecastRepository,unitProvider: UnitProvider)=
        HomeScreenViewModelFactory(forecastRepository,unitProvider)







}