package com.iti.mad42.weatherforcast.alerts.alertsRecyclerView.view

import com.iti.mad42.weatherforcast.model.WeatherAlerts

interface OnClickDeleteAlertListener {
    fun deleteAlert(weatherAlerts: WeatherAlerts)
}