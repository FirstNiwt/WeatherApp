package com.example.weatherapp.data.network

import com.example.weatherapp.data.db.entity.CurrentWeatherEntry
import com.example.weatherapp.data.db.entity.FutureWeatherEntry
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query



const val API_KEY = "5c3093d00be11bfdc4054d761135fabf"


interface OpenWeatherApiService {

    @GET("weather")
    fun getCurrentWeatherDataByLocationAsync(
        @Query("q") location: String,
        @Query("units") units: String = "metric",
        @Query("lang") languageOfResponse: String = "en",

    ): Deferred<CurrentWeatherEntry>

    @GET("weather")
    fun getCurrentWeatherDataByCoordinatesAsync(
        @Query("lat") positionLat: Double,
        @Query("lon") positionLon:Double,
        @Query("units") units: String = "metric",
        @Query("lang") languageOfResponse: String = "en",
    ):Deferred<CurrentWeatherEntry>


    @GET("onecall")
    fun getFutureWeatherDataByCoordinatesAsync(
        @Query("lat") positionLat: Double?,
        @Query("lon") positionLon: Double?,
        @Query("exclude") excludeFromCall: String = "minutely,alerts",
        @Query("units") units: String = "metric",
        @Query("lang") languageOfResponse: String = "en"

    ): Deferred<FutureWeatherEntry>

    companion object {
        operator fun invoke(
            connectivityInterceptor:ConnectivityInterceptor
        ): OpenWeatherApiService {
            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("appid", API_KEY)
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(connectivityInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OpenWeatherApiService::class.java)

        }
    }
}