package com.iti.mad42.weatherforcast.favourites.favouritesRecyclerView.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mad42.weatherforcast.model.FavouritePlace
import com.iti.mad42.weatherforcast.model.Repositry.Repositry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavouritePlacesViewModel (private val repo: Repositry) : ViewModel(),FavouritePlacesViewModelInterface {


    override fun getAllFavouritePlaces(): LiveData<List<FavouritePlace>> {
        return repo.getAllFavouritePlaces()
    }

    override fun deleteFavouritePlace(place: FavouritePlace) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteFavouritePlace(place)
        }
    }


}