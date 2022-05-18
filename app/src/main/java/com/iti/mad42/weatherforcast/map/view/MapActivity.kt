package com.iti.mad42.weatherforcast.map.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.iti.mad42.weatherforcast.MainActivity
import com.iti.mad42.weatherforcast.R
import com.iti.mad42.weatherforcast.Utilities.getCityText
import com.iti.mad42.weatherforcast.map.viewModel.MapViewModel
import com.iti.mad42.weatherforcast.map.viewModel.MapViewModelFactory
import com.iti.mad42.weatherforcast.map.viewModel.MapViewModelInterface
import com.iti.mad42.weatherforcast.model.Repositry.Repositry
import com.iti.mad42.weatherforcast.model.db.LocalDataSource
import com.iti.mad42.weatherforcast.model.network.RemoteDataSource

class MapActivity : AppCompatActivity() {

    private lateinit var mapFragment: SupportMapFragment
    private lateinit var btnDone: Button
    private lateinit var mapViewModel : MapViewModelInterface
    private lateinit var mapViewModelFactory: MapViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        createViewModel()
        initUI()
        setMapListener()
        setBtnClickListeners()




    }

    private fun createViewModel() {
        mapViewModelFactory = MapViewModelFactory(
            Repositry.getInstance(
                RemoteDataSource.getInstance(),
                LocalDataSource.getInstance(this),
                this)
        )

        mapViewModel = ViewModelProvider(this,mapViewModelFactory)[MapViewModel::class.java]

    }

    private fun initUI() {
        mapFragment = (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)!!
        btnDone = findViewById<Button>(R.id.btnDone)
    }
    private fun setBtnClickListeners() {
        btnDone.setOnClickListener {
            if (intent.getBooleanExtra("fromFavouriteScreen",false)) {
                val language = com.iti.mad42.weatherforcast.Utilities.getSharedPreferences(this)
                    .getString(getString(R.string.languageSetting), "en")
                val name = getCityText(this,mapViewModel.getLocationValue().latitude,mapViewModel.getLocationValue().longitude,language!!)
                mapViewModel.insertFavouritePlaceInDB(name)
                navigateToFavoriteScreen()
            } else {
                navigateToHome(mapViewModel.getLocationValue())
            }
        }
    }

    private fun setMapListener() {
        mapFragment.getMapAsync(OnMapReadyCallback { googleMap ->
            val location = LatLng(31.037933, 31.381523)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10.0f))
            googleMap.uiSettings.isZoomControlsEnabled = true

            googleMap.setOnMapClickListener { location ->
                googleMap.clear()
                googleMap.addMarker(MarkerOptions().position(location))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12.0f))
                mapViewModel.setLocationValue(location)
                btnDone.visibility = View.VISIBLE
            }
        })
    }

    private fun navigateToHome(location : LatLng) {
        val editor = com.iti.mad42.weatherforcast.Utilities.getSharedPreferences(this).edit()
        editor.putFloat("lat", location.latitude.toFloat())
        editor.putFloat("lon", location.longitude.toFloat())
        editor.commit()

        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToFavoriteScreen() {
        finish()
    }
}