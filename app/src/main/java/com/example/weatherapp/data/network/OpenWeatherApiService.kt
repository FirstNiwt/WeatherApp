package com.example.weatherapp.data.network

import com.example.weatherapp.data.db.entity.CurrentWeatherEntry
import com.example.weatherapp.data.network.ConnectivityInterceptor
import com.example.weatherapp.data.network.ConnectivityInterceptorImpl

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


//CALL = https://api.openweathermap.org/data/2.5/weather?q=Olkusz&appid=5c3093d00be11bfdc4054d761135fabf&lang=en

//RESPOSNE = {"coord":{"lon":19.565,"lat":50.2813},"weather":[{"id":804,"main":"Clouds","description":"overcast clouds","icon":"04n"}],"base":"stations","main":{"temp":272.35,"feels_like":269.62,"temp_min":270.3,"temp_max":273.48,"pressure":1013,"humidity":97,"sea_level":1013,"grnd_level":967},"visibility":636,"wind":{"speed":2.11,"deg":334,"gust":4.62},"clouds":{"all":97},"dt":1638818483,"sys":{"type":2,"id":265524,"country":"PL","sunrise":1638771972,"sunset":1638801584},"timezone":3600,"id":3090146,"name":"Olkusz","cod":200}

const val API_KEY = "5c3093d00be11bfdc4054d761135fabf"


interface OpenWeatherApiService {

    @GET("weather")
    fun getCurrentWeatherData(
        @Query("q") location: String,
        @Query("units") units: String = "metric",
        @Query("lang") languageOfResponse: String = "en",

    ): Deferred<CurrentWeatherEntry>


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