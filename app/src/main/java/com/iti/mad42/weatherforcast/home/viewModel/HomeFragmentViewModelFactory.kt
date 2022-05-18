package com.iti.mad42.weatherforcast.home.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.mad42.weatherforcast.Utilities.LocationHelper
import com.iti.mad42.weatherforcast.model.Repositry.RepositryInterface

class HomeFragmentViewModelFactory (val repo: RepositryInterface, val locationHelper: LocationHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeFragmentViewModel::class.java))
            return HomeFragmentViewModel(repo,locationHelper) as T
        else
            throw IllegalArgumentException("Not Found")
    }
}