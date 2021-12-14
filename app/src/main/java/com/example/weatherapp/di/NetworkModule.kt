package com.example.weatherapp.di

import com.example.weatherapp.data.CurrentWeatherDao
import com.example.weatherapp.data.network.ConnectivityInterceptor
import com.example.weatherapp.data.network.ConnectivityInterceptorImpl
import com.example.weatherapp.data.network.WeatherNetworkDataSource
import com.example.weatherapp.data.network.WeatherNetworkDataSourceImpl
import com.example.weatherapp.data.repository.ForecastRepository
import com.example.weatherapp.data.repository.ForecastRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    @Binds
    abstract fun providesConnectivityInterceptor(connectivityInterceptorImpl: ConnectivityInterceptorImpl):ConnectivityInterceptor


    @Binds
    abstract fun providesForecastRepository(forecastRepositoryImpl: ForecastRepositoryImpl): ForecastRepository


    @Binds
    abstract fun providesWeatherNetworkDataSource(weatherNetworkDataSourceImpl: WeatherNetworkDataSourceImpl): WeatherNetworkDataSource



}