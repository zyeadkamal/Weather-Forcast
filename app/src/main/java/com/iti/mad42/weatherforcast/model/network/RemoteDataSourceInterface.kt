package com.iti.mad42.weatherforcast.model.network

import com.iti.mad42.weatherforcast.model.WeatherApi
import retrofit2.Response

interface RemoteDataSourceInterface {
    suspend fun fetchWeather(lat:Double , lon:Double, units:String , lang:String): Response<WeatherApi>

}