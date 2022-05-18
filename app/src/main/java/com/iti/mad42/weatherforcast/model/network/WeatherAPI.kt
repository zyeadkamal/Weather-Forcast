package com.iti.mad42.weatherforcast.model.network

import com.iti.mad42.weatherforcast.model.WeatherApi
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET("onecall")
    suspend fun getWeather(@Query("lat")lat:Double , @Query("lon")lon:Double,@Query("units")units:String,@Query("lang")lang:String,@Query("appid")appid:String): Response<WeatherApi>
}