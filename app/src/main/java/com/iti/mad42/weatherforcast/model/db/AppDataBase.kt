package com.iti.mad42.weatherforcast.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.iti.mad42.weatherforcast.Utilities.Converter
import com.iti.mad42.weatherforcast.model.FavouritePlace
import com.iti.mad42.weatherforcast.model.WeatherAlerts
import com.iti.mad42.weatherforcast.model.WeatherApi

@TypeConverters(Converter::class)
@Database(entities = [WeatherApi::class,FavouritePlace::class,WeatherAlerts::class], version = 2)
abstract class AppDataBase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao
    abstract fun favouritePlacesDao(): FavouritePlaceDao
    abstract fun alertsDao(): AlertsDao


    companion object {
        private val DATABASE_NAME = "Weather_DB"
        private var instance: AppDataBase? = null
        @Synchronized
        fun getInstance(context: Context): AppDataBase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java, DATABASE_NAME
                ).build()
            }
            return instance
        }
    }
}