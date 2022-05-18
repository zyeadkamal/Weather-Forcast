package com.iti.mad42.weatherforcast.alerts.alertsRecyclerView.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.mad42.weatherforcast.model.Repositry.Repositry

class AlertsViewModelFactory (val repo: Repositry) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlertsViewModel::class.java))
            return AlertsViewModel(repo) as T
        else
            throw IllegalArgumentException("Not Found")
    }
}