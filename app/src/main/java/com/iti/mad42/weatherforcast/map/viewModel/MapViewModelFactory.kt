package com.iti.mad42.weatherforcast.map.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.mad42.weatherforcast.model.Repositry.Repositry

class MapViewModelFactory (private val repo: Repositry): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MapViewModel(repo) as T
    }
}