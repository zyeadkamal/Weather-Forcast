package com.iti.mad42.weatherforcast.map.viewModel

import com.google.android.gms.maps.model.LatLng

interface MapViewModelInterface {
    fun setLocationValue(location : LatLng)
    fun getLocationValue():LatLng
    fun insertFavouritePlaceInDB(name : String)
}