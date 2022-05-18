package com.iti.mad42.weatherforcast.home.viewModel

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mad42.weatherforcast.Utilities.LocationHelper
import com.iti.mad42.weatherforcast.model.Repositry.Repositry
import com.iti.mad42.weatherforcast.model.Repositry.RepositryInterface
import com.iti.mad42.weatherforcast.model.WeatherApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragmentViewModel (private val repo : RepositryInterface , private val locationHelper: LocationHelper) : ViewModel(), HomeFragmentViewModelInterface {

    private val _weather = MutableLiveData<WeatherApi>()
    var weather : LiveData<WeatherApi> = _weather

    override fun fetchingWeather(lat: Double, long: Double, language: String, units: String) {
        viewModelScope.launch (Dispatchers.IO){
            var result = repo.fetchWeather(lat,long,units,language)
            if (result.isSuccessful){
                _weather.postValue(result.body())
                repo.insertCurrentWeather(result.body()!!)
            }else{
                Log.i("Network", "fetchingWeather: error while fetching weather from API")
            }
        }
    }

    override fun getWeatherFromDataBase() {
        viewModelScope.launch(Dispatchers.IO) {
            _weather.postValue(
                repo.getCurrentWeather()
            )
        }
    }

    override fun getFreshLocation(){
        locationHelper.getFreshLocation()
    }

    override fun getLastLocation(): LiveData<Location> {
        return locationHelper.locationList
    }

    override fun returnWeather(): LiveData<WeatherApi> {
        return weather
    }

}