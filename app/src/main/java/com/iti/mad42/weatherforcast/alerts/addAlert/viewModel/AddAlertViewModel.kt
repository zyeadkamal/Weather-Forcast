package com.iti.mad42.weatherforcast.alerts.addAlert.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mad42.weatherforcast.model.Repositry.Repositry
import com.iti.mad42.weatherforcast.model.WeatherAlerts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddAlertViewModel (private val repo: Repositry) : ViewModel(),AddAlertViewModelInterface {


    private var startDate:Long = 0
    private var endDate:Long = 0
    private var startDateString:String = ""
    private var endDateString:String = ""
    private var language: String = "en"
    private var alertTime :Long = 0
    private var alertType :String = ""
    private var alertDays: List<String> = emptyList()
//    private var lat: Double = 0.0
//    private var lon: Double = 0.0

    override fun insertAlert() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertAlert(WeatherAlerts(null,startDate, endDate, alertTime, alertType, alertDays))
        }
    }

    override fun assignStartDate(date: Long) {
        startDate = date
    }

    override fun returnStartDate(): Long {
        return startDate
    }

    override fun assignEndDate(endDate: Long) {
        this.endDate = endDate
    }

    override fun returnEndDate(): Long {
        return endDate
    }

    override fun assignStartDateString(date: String) {
        startDateString = date
    }

    override fun returnStartDateString(): String {
        return startDateString
    }

    override fun assignEndDateString(date: String) {
        endDateString = date
    }

    override fun returnEndDateString(): String {
        return endDateString
    }

    override fun assignLanguage(lang: String) {
        language = lang
    }

    override fun returnLanguage(): String {
        return language
    }

    override fun assignAlertTime(time: Long) {
        alertTime = time
    }

    override fun returnAlertTime(): Long {
        return alertTime
    }

    override fun assignAlertType(type: String) {
        alertType = type
    }

    override fun returnAlertType(): String {
        return alertType
    }

    override fun assignAlertDays(days: List<String>) {
        alertDays = days
    }

    override fun returnAlertDays(): List<String> {
        return alertDays
    }

//    override fun assignLat(lat: Double) {
//        this.lat = lat
//    }
//
//    override fun returnLat(): Double {
//        return lat
//    }
//
//    override fun assignLon(lon: Double) {
//        this.lon = lon
//    }
//
//    override fun returnLon(): Double {
//        return lon
//    }


}