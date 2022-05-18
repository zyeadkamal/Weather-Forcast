package com.iti.mad42.weatherforcast.model.network

import com.iti.mad42.weatherforcast.model.WeatherApi
import retrofit2.Response

class RemoteDataSource private constructor() : RemoteDataSourceInterface {

    private val appID="3d0b0de49fc523ca76e35ed208b38173"

    companion object{
        private var instance: RemoteDataSource? = null
        fun getInstance(): RemoteDataSource {
            if (instance == null){
                instance = RemoteDataSource()
            }
            return instance as RemoteDataSource
        }
    }

    override suspend fun fetchWeather(
        lat: Double,
        lon: Double,
        units: String,
        lang: String
    ): Response<WeatherApi> {
        println(lat)
        println(lon)
        println(units)
        println(lang)
        return RetrofitHelper.getInstance().create(WeatherAPI::class.java).getWeather(lat,lon,units,lang, appID)
    }

}