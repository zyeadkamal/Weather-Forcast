package com.iti.mad42.weatherforcast.model.Repositry

import androidx.lifecycle.LiveData
import com.iti.mad42.weatherforcast.model.FavouritePlace
import com.iti.mad42.weatherforcast.model.WeatherAlerts
import com.iti.mad42.weatherforcast.model.WeatherApi
import io.reactivex.Single
import retrofit2.Response

interface RepositryInterface {
    suspend fun fetchWeather(lat:Double , lon:Double, units:String , lang:String): Response<WeatherApi>
    suspend fun insertCurrentWeather(weather: WeatherApi)
    suspend fun getCurrentWeather(): WeatherApi
    fun insertFavouritePlace(place : FavouritePlace)
    fun getAllFavouritePlaces() : LiveData<List<FavouritePlace>>
    fun deleteFavouritePlace(place: FavouritePlace)
    fun insertAlert(alert: WeatherAlerts)
    fun deleteAlert(alert: WeatherAlerts)
    fun getAllStoredAlerts(): LiveData<List<WeatherAlerts>>
    fun getAllAlertsFlow(): Single<List<WeatherAlerts>>

}