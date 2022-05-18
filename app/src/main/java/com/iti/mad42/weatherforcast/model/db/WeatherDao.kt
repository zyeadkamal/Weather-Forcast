package com.iti.mad42.weatherforcast.model.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.iti.mad42.weatherforcast.model.WeatherApi

@Dao
interface WeatherDao {
    @Query("select * from weather where id = 0")
    suspend fun getCurrentWeather(): WeatherApi

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentWeather(weather: WeatherApi)
}