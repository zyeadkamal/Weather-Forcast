package com.iti.mad42.weatherforcast.model.Repositry

import android.content.Context
import androidx.lifecycle.LiveData
import com.iti.mad42.weatherforcast.model.FavouritePlace
import com.iti.mad42.weatherforcast.model.WeatherAlerts
import com.iti.mad42.weatherforcast.model.WeatherApi
import com.iti.mad42.weatherforcast.model.db.LocalDataSourceInterface
import com.iti.mad42.weatherforcast.model.network.RemoteDataSourceInterface
import io.reactivex.Single
import retrofit2.Response

class Repositry private constructor(var remoteSource: RemoteDataSourceInterface,var localSource: LocalDataSourceInterface,  var context: Context) : RepositryInterface {

    companion object{
        private var instance : Repositry?= null
        fun getInstance( remoteSource: RemoteDataSourceInterface, localSource: LocalDataSourceInterface, context: Context) : Repositry {
            if (instance == null) {
                instance = Repositry(remoteSource, localSource ,context)
            }
            return instance as Repositry
        }

    }

    override suspend fun fetchWeather(
        lat: Double,
        lon: Double,
        units: String,
        lang: String
    ): Response<WeatherApi> {
        return remoteSource.fetchWeather(lat,lon, units, lang)
    }

    override suspend fun insertCurrentWeather(weather: WeatherApi) {
        localSource.insertCurrentWeather(weather)
    }

    override suspend fun getCurrentWeather() = localSource.getCurrentWeather()

    override fun insertFavouritePlace(place: FavouritePlace) {
            localSource.insertFavouritePlace(place)
    }

    override fun deleteFavouritePlace(place: FavouritePlace) {
        localSource.deleteFavouritePlace(place)
    }

    override fun getAllFavouritePlaces(): LiveData<List<FavouritePlace>> {
        return localSource.gatAllFavouritePlaces()
    }

    override fun insertAlert(alert: WeatherAlerts) {
        localSource.insertAlert(alert)
    }

    override fun deleteAlert(alert: WeatherAlerts) {
        localSource.deleteAlert(alert)
    }

    override fun getAllStoredAlerts(): LiveData<List<WeatherAlerts>> {
        return localSource.allStoredAlerts()
    }

    override fun getAllAlertsFlow(): Single<List<WeatherAlerts>> {
        return localSource.getAllAlertsFlow()
    }


}