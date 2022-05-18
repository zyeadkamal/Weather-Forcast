package com.iti.mad42.weatherforcast.Utilities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class LocationHelper (val fragment: Fragment) {

    private var _locationList = MutableLiveData<Location>()
    val locationList = _locationList

    private var _denyPermission = MutableLiveData<String>()
    private val denyPermission = _denyPermission

    private var fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(fragment.requireActivity())

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val location = locationResult.lastLocation
            println(location.latitude)
            println(location.longitude)
            _locationList.postValue(location)
            fusedLocationProviderClient.removeLocationUpdates(this)
        }
    }

    fun checkPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            fragment.requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
            fragment.requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }

    fun requestPermission() {
        val requestPermissionLauncher: ActivityResultLauncher<String> =
            fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    getFreshLocation()
                } else {
                    denyPermission.postValue("denied")
                }
            }

        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    fun isLocationEnabled(): Boolean {
        val locationManager =
            fragment.requireActivity().application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
    }


    fun getFreshLocation() {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        locationRequest.interval = 120000
        locationRequest.fastestInterval = 120000
        if (checkPermission()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.apply {
                    requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        Looper.getMainLooper()
                    )
                }
            } else {
                fragment.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        } else {
            requestPermission()
        }
    }







//    private fun stopLocationUpdates() {
//        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
//    }
}