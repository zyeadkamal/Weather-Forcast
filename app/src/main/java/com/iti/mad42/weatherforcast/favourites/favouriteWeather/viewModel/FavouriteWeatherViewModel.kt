package com.iti.mad42.weatherforcast.favourites.favouriteWeather.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mad42.weatherforcast.model.Repositry.Repositry
import com.iti.mad42.weatherforcast.model.WeatherApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavouriteWeatherViewModel (private val repo: Repositry) : ViewModel() , FavouriteWeatherViewModelInterface {

    private val _weather = MutableLiveData<WeatherApi>()
    val weather : LiveData<WeatherApi> = _weather

    override fun returnWeather(): LiveData<WeatherApi> {
        return weather
    }

    override fun fetchingWeather(lat: Double, long: Double, language: String, units: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _weather.postValue(repo.fetchWeather(lat, long, units, language).body())
        }
    }
}