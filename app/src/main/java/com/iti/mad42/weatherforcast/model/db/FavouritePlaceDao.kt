package com.iti.mad42.weatherforcast.model.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.iti.mad42.weatherforcast.model.FavouritePlace

@Dao
interface FavouritePlaceDao {

    @Query("select * from favouritePlaces")
    fun getAllFavouritePlaces(): LiveData<List<FavouritePlace>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavouritePlace(place: FavouritePlace)

    @Delete
    fun deleteFavouritePlace(place: FavouritePlace)
}