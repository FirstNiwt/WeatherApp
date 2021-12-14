package com.example.weatherapp.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherapp.data.CurrentWeatherDao
import com.example.weatherapp.data.db.ForecastDatabase
import com.example.weatherapp.data.network.*
import com.example.weatherapp.data.repository.ForecastRepository
import com.example.weatherapp.data.repository.ForecastRepositoryImpl
import com.example.weatherapp.ui.weather.current.CurrentWeatherViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
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
    fun providesWeatherApiService(connectivityInterceptor:
    ConnectivityInterceptor) = OpenWeatherApiService(connectivityInterceptor)

    @Provides
    @Singleton
    fun provideConnectivityInterceptorImpl(@ApplicationContext app: Context) = ConnectivityInterceptorImpl(app)


    @Provides
    @Singleton
    fun provideForecastRepositoryImpl(currentWeatherDao: CurrentWeatherDao,weatherNetworkDataSource:
    WeatherNetworkDataSource) = ForecastRepositoryImpl(currentWeatherDao, weatherNetworkDataSource)

    @Provides
    @Singleton
    fun provideWeatherNetworkDataSourceImpl(openWeatherApiService: OpenWeatherApiService)
    = WeatherNetworkDataSourceImpl(openWeatherApiService)

    @Provides
    @Singleton
    fun provideCurrentWeatherViewModelFactory(forecastRepository: ForecastRepository)
    = CurrentWeatherViewModelFactory(forecastRepository)









}