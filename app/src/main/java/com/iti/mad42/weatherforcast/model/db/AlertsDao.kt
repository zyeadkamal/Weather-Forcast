package com.iti.mad42.weatherforcast.model.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.iti.mad42.weatherforcast.model.WeatherAlerts
import io.reactivex.Single

@Dao
interface AlertsDao {
    @Query("select * from alerts")
    fun getAllAlerts(): LiveData<List<WeatherAlerts>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAlert(alert: WeatherAlerts)

    @Delete
    fun deleteAlert(alert: WeatherAlerts)

    @Query("select * from alerts")
    fun getAllAlertsFlow(): Single<List<WeatherAlerts>>
}