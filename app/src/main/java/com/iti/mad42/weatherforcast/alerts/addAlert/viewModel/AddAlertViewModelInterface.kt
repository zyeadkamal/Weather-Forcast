package com.iti.mad42.weatherforcast.alerts.addAlert.viewModel

import com.iti.mad42.weatherforcast.model.WeatherAlerts

interface AddAlertViewModelInterface {
    fun insertAlert()
    fun assignStartDate(date : Long)
    fun returnStartDate() : Long
    fun assignEndDate(endDate : Long)
    fun returnEndDate() : Long
    fun assignStartDateString(date : String)
    fun returnStartDateString() : String
    fun assignEndDateString(date : String)
    fun returnEndDateString() : String
    fun assignLanguage(lang : String)
    fun returnLanguage() : String
    fun assignAlertTime(time : Long)
    fun returnAlertTime() : Long
    fun assignAlertType(type : String)
    fun returnAlertType() : String
    fun assignAlertDays(days : List<String>)
    fun returnAlertDays() : List<String>
//    fun assignLat(lat : Double)
//    fun returnLat() : Double
//    fun assignLon(lon : Double)
//    fun returnLon() : Double




}