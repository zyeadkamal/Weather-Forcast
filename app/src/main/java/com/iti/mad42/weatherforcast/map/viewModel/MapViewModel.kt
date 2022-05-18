package com.iti.mad42.weatherforcast.map.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.iti.mad42.weatherforcast.model.FavouritePlace
import com.iti.mad42.weatherforcast.model.Repositry.Repositry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MapViewModel (private val repo : Repositry) : ViewModel(),MapViewModelInterface {

    private var location : LatLng? = null
    override fun setLocationValue(location: LatLng) {
        this.location = location
    }

    override fun getLocationValue(): LatLng {
        return location?:LatLng(31.037933, 31.381523)
    }

    override fun insertFavouritePlaceInDB(name : String) {
        viewModelScope.launch(Dispatchers.IO) {
                repo.insertFavouritePlace(FavouritePlace(name,getLocationValue().latitude,getLocationValue().longitude))
            this.cancel()
        }
    }
}