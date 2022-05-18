package com.iti.mad42.weatherforcast.favourites.favouritesRecyclerView.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.mad42.weatherforcast.model.Repositry.Repositry

class FavouritePlacesViewModelFactory (val repo: Repositry) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavouritePlacesViewModel::class.java))
            return FavouritePlacesViewModel(repo) as T
        else
            throw IllegalArgumentException("Not Found")
    }
}