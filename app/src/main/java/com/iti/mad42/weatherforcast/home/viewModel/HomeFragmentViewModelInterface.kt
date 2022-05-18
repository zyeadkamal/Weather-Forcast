package com.iti.mad42.weatherforcast.home.viewModel

import android.location.Location
import androidx.lifecycle.LiveData
import com.iti.mad42.weatherforcast.model.WeatherApi

interface HomeFragmentViewModelInterface {
    fun fetchingWeather(lat: Double, long: Double, language: String, units: String)
    fun getWeatherFromDataBase()
    fun getFreshLocation()
    fun getLastLocation():LiveData<Location>
    fun returnWeather():LiveData<WeatherApi>
}