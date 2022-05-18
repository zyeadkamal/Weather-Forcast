package com.iti.mad42.weatherforcast.alerts.alertsRecyclerView.viewModel

import androidx.lifecycle.LiveData
import com.iti.mad42.weatherforcast.model.WeatherAlerts

interface AlertsViewModelInterface {

    fun getAllAlerts() : LiveData<List<WeatherAlerts>>
    fun deleteAlert(alert: WeatherAlerts)
}