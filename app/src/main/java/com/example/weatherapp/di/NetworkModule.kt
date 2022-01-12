package com.example.weatherapp.di

import com.example.weatherapp.data.network.ConnectivityInterceptor
import com.example.weatherapp.data.network.ConnectivityInterceptorImpl
import com.example.weatherapp.data.network.WeatherNetworkDataSource
import com.example.weatherapp.data.network.WeatherNetworkDataSourceImpl
import com.example.weatherapp.data.provider.LocationProvider
import com.example.weatherapp.data.provider.LocationProviderImpl
import com.example.weatherapp.data.provider.UnitProvider
import com.example.weatherapp.data.provider.UnitProviderImpl
import com.example.weatherapp.data.repository.ForecastRepository
import com.example.weatherapp.data.repository.ForecastRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent



@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    @Binds
    abstract fun providesConnectivityInterceptor(connectivityInterceptorImpl: ConnectivityInterceptorImpl):ConnectivityInterceptor


    @Binds
    abstract fun providesForecastRepository(forecastRepositoryImpl: ForecastRepositoryImpl): ForecastRepository


    @Binds
    abstract fun providesWeatherNetworkDataSource(weatherNetworkDataSourceImpl: WeatherNetworkDataSourceImpl): WeatherNetworkDataSource

    @Binds
    abstract fun providesUnitProvider(unitProviderImpl:UnitProviderImpl):UnitProvider

    @Binds
    abstract fun provideLocationProvider(locationProviderImpl: LocationProviderImpl): LocationProvider


}