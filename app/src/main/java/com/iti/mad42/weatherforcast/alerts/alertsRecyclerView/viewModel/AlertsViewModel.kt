package com.iti.mad42.weatherforcast.alerts.alertsRecyclerView.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mad42.weatherforcast.model.Repositry.Repositry
import com.iti.mad42.weatherforcast.model.WeatherAlerts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlertsViewModel (private val repo: Repositry) : ViewModel(),AlertsViewModelInterface {


    override fun getAllAlerts() : LiveData<List<WeatherAlerts>> {
        return repo.getAllStoredAlerts()
    }

    override fun deleteAlert(alert: WeatherAlerts) {
        viewModelScope.launch (Dispatchers.IO){
            repo.deleteAlert(alert)
        }
    }

}