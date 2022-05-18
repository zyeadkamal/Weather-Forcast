package com.iti.mad42.weatherforcast.model.db

import androidx.lifecycle.LiveData
import com.iti.mad42.weatherforcast.model.FavouritePlace
import com.iti.mad42.weatherforcast.model.WeatherAlerts
import com.iti.mad42.weatherforcast.model.WeatherApi
import io.reactivex.Single

interface LocalDataSourceInterface {

    suspend fun insertCurrentWeather(weather: WeatherApi)
    suspend fun getCurrentWeather(): WeatherApi
    fun insertFavouritePlace(place : FavouritePlace)
    fun gatAllFavouritePlaces() : LiveData<List<FavouritePlace>>
    fun deleteFavouritePlace(place: FavouritePlace)
    fun insertAlert(alert: WeatherAlerts)
    fun deleteAlert(alert: WeatherAlerts)
    fun allStoredAlerts(): LiveData<List<WeatherAlerts>>
    fun getAllAlertsFlow(): Single<List<WeatherAlerts>>




}