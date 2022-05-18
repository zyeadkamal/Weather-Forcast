package com.iti.mad42.weatherforcast.Utilities

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.iti.mad42.weatherforcast.model.Alerts
import com.iti.mad42.weatherforcast.model.Current
import com.iti.mad42.weatherforcast.model.Daily
import com.iti.mad42.weatherforcast.model.Hourly

class Converter {

    @TypeConverter
    fun currentWeatherToJson(current: Current?): String {
        return Gson().toJson(current)
    }

    @TypeConverter
    fun jsonToCurrentWeather(currentString: String): Current {
        return Gson().fromJson(currentString, Current::class.java)
    }


    @TypeConverter
    fun hourlyWeatherListToJson(hourly: List<Hourly>?): String {
        return Gson().toJson(hourly)
    }

    @TypeConverter
    fun jsonToHourlyWeatherList(hourlyString: String): List<Hourly> {
        return Gson().fromJson(hourlyString, Array<Hourly>::class.java).toList()
    }

    @TypeConverter
    fun dailyWeatherListToJson(dailyList: List<Daily>): String {
        return Gson().toJson(dailyList)
    }

    @TypeConverter
    fun jsonToDailyWeatherList(dailyString: String): List<Daily> {
        return Gson().fromJson(dailyString, Array<Daily>::class.java).toList()
    }

    @TypeConverter
    fun alertListToJson(alertList: List<Alerts>?): String {
        return Gson().toJson(alertList)
    }

    @TypeConverter
    fun jsonToAlertList(alertString: String?): List<Alerts>? {
        alertString?.let {
            return Gson().fromJson(alertString, Array<Alerts>::class.java)?.toList()
        }
        return emptyList()
    }

    @TypeConverter
    fun alertDaysListToJson(alertDays: List<String>): String? {
        return Gson().toJson(alertDays)
    }

    @TypeConverter
    fun jsonToAlertDaysList(alertDays: String): List<String> {
        return Gson().fromJson(alertDays, Array<String>::class.java).toList()
    }


}