package com.example.weatherapp.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherapp.data.CurrentWeatherDao
import com.example.weatherapp.data.FutureWeatherDao
import com.example.weatherapp.data.db.ForecastDatabase
import com.example.weatherapp.data.network.*
import com.example.weatherapp.data.repository.ForecastRepository
import com.example.weatherapp.data.repository.ForecastRepositoryImpl
import com.example.weatherapp.ui.weather.current.CurrentWeatherViewModelFactory
import com.example.weatherapp.ui.weather.weekly.DailyWeatherListFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.Future
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
    fun provideForecastRepositoryImpl(currentWeatherDao: CurrentWeatherDao,futureWeatherDao:FutureWeatherDao,weatherNetworkDataSource:
    WeatherNetworkDataSource) = ForecastRepositoryImpl(currentWeatherDao,futureWeatherDao, weatherNetworkDataSource)

    @Provides
    @Singleton
    fun provideWeatherNetworkDataSourceImpl(openWeatherApiService: OpenWeatherApiService)
    = WeatherNetworkDataSourceImpl(openWeatherApiService)

    @Provides
    @Singleton
    fun provideCurrentWeatherViewModelFactory(forecastRepository: ForecastRepository)
    = CurrentWeatherViewModelFactory(forecastRepository)

    @Provides
    @Singleton
    fun provideDailyWeatherListViewModelFactory(forecastRepository: ForecastRepository)
            = DailyWeatherListFactory(forecastRepository)








}