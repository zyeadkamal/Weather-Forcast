package com.iti.mad42.weatherforcast.alerts.addAlert.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.mad42.weatherforcast.model.Repositry.Repositry

class AddAlertViewModelFactory (val repo: Repositry) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddAlertViewModel::class.java))
            return AddAlertViewModel(repo) as T
        else
            throw IllegalArgumentException("Not Found")
    }
}