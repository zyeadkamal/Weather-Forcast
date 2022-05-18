package com.iti.mad42.weatherforcast.favourites.favouriteWeather.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.mad42.weatherforcast.model.Repositry.Repositry

class FavouriteWeatherViewModelFactory (val repo: Repositry) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavouriteWeatherViewModel::class.java))
            return FavouriteWeatherViewModel(repo) as T
        else
            throw IllegalArgumentException("Not Found")
    }
}