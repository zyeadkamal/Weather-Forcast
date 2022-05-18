package com.iti.mad42.weatherforcast.model.db

import android.content.Context
import androidx.lifecycle.LiveData
import com.iti.mad42.weatherforcast.model.FavouritePlace
import com.iti.mad42.weatherforcast.model.WeatherAlerts
import com.iti.mad42.weatherforcast.model.WeatherApi
import com.iti.mad42.weatherforcast.model.network.RemoteDataSource
import io.reactivex.Single

class LocalDataSource (context : Context) : LocalDataSourceInterface {

    private val db: AppDataBase? = AppDataBase.getInstance(context)
    private val weatherDao : WeatherDao? = db?.weatherDao()
    private val favouritePlaceDao = db?.favouritePlacesDao()
    private val alertsDao = db?.alertsDao()


    companion object{
        private var instance: LocalDataSource? = null
        fun getInstance(context : Context): LocalDataSource {
            if (instance == null){
                instance = LocalDataSource(context)
            }
            return instance as LocalDataSource
        }
    }

    override suspend fun insertCurrentWeather(weather: WeatherApi) {
        weatherDao?.insertCurrentWeather(weather)
    }

    override suspend fun getCurrentWeather() = weatherDao?.getCurrentWeather()!!

    override fun insertFavouritePlace(place: FavouritePlace) {
        favouritePlaceDao?.insertFavouritePlace(place)
    }

    override fun gatAllFavouritePlaces(): LiveData<List<FavouritePlace>> {
        return favouritePlaceDao?.getAllFavouritePlaces()!!
    }

    override fun deleteFavouritePlace(place: FavouritePlace) {
        favouritePlaceDao?.deleteFavouritePlace(place)
    }

    override fun insertAlert(alert: WeatherAlerts) {
        alertsDao?.insertAlert(alert)
    }

    override fun deleteAlert(alert: WeatherAlerts) {
        alertsDao?.deleteAlert(alert)
    }

    override fun allStoredAlerts(): LiveData<List<WeatherAlerts>> {
        return alertsDao?.getAllAlerts()!!
    }

    override fun getAllAlertsFlow(): Single<List<WeatherAlerts>> {
        return alertsDao?.getAllAlertsFlow()!!
    }

}