package com.iti.mad42.weatherforcast.favourites.favouriteWeather.viewModel

import androidx.lifecycle.LiveData
import com.iti.mad42.weatherforcast.model.WeatherApi

interface FavouriteWeatherViewModelInterface {
    fun returnWeather() : LiveData<WeatherApi>
    fun fetchingWeather(lat: Double, long: Double, language: String, units: String)
}