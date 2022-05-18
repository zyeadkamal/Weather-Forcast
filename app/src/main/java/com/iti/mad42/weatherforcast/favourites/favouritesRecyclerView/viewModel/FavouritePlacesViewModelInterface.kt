package com.iti.mad42.weatherforcast.favourites.favouritesRecyclerView.viewModel

import androidx.lifecycle.LiveData
import com.iti.mad42.weatherforcast.model.FavouritePlace

interface FavouritePlacesViewModelInterface {

    fun getAllFavouritePlaces() : LiveData<List<FavouritePlace>>
    fun deleteFavouritePlace(place: FavouritePlace)
}