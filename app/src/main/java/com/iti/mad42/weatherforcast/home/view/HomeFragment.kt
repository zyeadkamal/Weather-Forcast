package com.iti.mad42.weatherforcast.home.view

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iti.mad42.weatherforcast.R
import com.iti.mad42.weatherforcast.Utilities.*
import com.iti.mad42.weatherforcast.home.viewModel.HomeFragmentViewModel
import com.iti.mad42.weatherforcast.home.viewModel.HomeFragmentViewModelFactory
import com.iti.mad42.weatherforcast.model.Repositry.Repositry
import com.iti.mad42.weatherforcast.model.WeatherApi
import com.iti.mad42.weatherforcast.model.db.LocalDataSource
import com.iti.mad42.weatherforcast.model.network.RemoteDataSource
import com.iti.mad42.weatherforcast.Utilities.NetworkChangeReceiver.Companion.isAppOnline
import com.iti.mad42.weatherforcast.home.viewModel.HomeFragmentViewModelInterface
import com.iti.mad42.weatherforcast.map.view.MapActivity
import com.iti.mad42.weatherforcast.Utilities.Utilities.Companion.tempeUnit
import com.iti.mad42.weatherforcast.Utilities.Utilities.Companion.windSpeedUnit


class HomeFragment : Fragment() {

    private lateinit var homeFragmentViewModelFactory: HomeFragmentViewModelFactory
    private lateinit var homeFragmentViewModel: HomeFragmentViewModelInterface

    private lateinit var tvCityName : TextView
    private lateinit var tvCurrentTemp : TextView
    private lateinit var tvWeatherState : TextView
    private lateinit var tvCurrentMaxTemp : TextView
    private lateinit var tvCurrentMinTemp : TextView



    private lateinit var hourlyRecyclerViewAdapter: HourlyRecyclerViewAdapter
    private lateinit var dailyRecyclerViewAdapter: DailyRecyclerViewAdapter

    private lateinit var tvCloud : TextView
    private lateinit var tvCloudValue : TextView
    private lateinit var ivCloud : ImageView

    private lateinit var tvPressure : TextView
    private lateinit var tvPressureValue : TextView
    private lateinit var ivPressure : ImageView

    private lateinit var tvWind : TextView
    private lateinit var tvWindValue : TextView
    private lateinit var ivWind : ImageView

    private lateinit var tvHumidity : TextView
    private lateinit var tvHumidityValue : TextView
    private lateinit var ivHumidity : ImageView

    private lateinit var tvVisibility : TextView
    private lateinit var tvVisibilityValue : TextView
    private lateinit var ivVisibility : ImageView

    private lateinit var tvUltraViolet : TextView
    private lateinit var tvUltraVioletValue : TextView
    private lateinit var ivUltraViolet : ImageView

    private lateinit var locationHelper : LocationHelper



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI(view)

        locationHelper = LocationHelper(this)

        createViewModel()

        if (isAppOnline){

            val local = getCurrentLocale(requireContext())
            val language = getSharedPreferences(requireContext()).getString("languageSetting","ar")!!
            Log.e("language", "onViewCreated: $language" )
            println(language)
            val units = getSharedPreferences(requireContext()).getString(
                getString(R.string.unitsSetting),
                "metric"
            )!!

            if (!thereIsNoLatAndLon(requireContext())) {
                val sharedPrefs = getSharedPreferences(requireContext())
                homeFragmentViewModel.fetchingWeather(sharedPrefs.getFloat("lat",31.037933f).toDouble(), sharedPrefs.getFloat("lon",31.381523f).toDouble(), language, units)
            }else {
                when(getLocationMethod(requireContext())){
                    "map" -> {
                        navigateToMap()
                    }
                    "gps" -> {
                        if (locationHelper.checkPermission() && locationHelper.isLocationEnabled()) {
                            homeFragmentViewModel.getFreshLocation()
                            homeFragmentViewModel.getLastLocation().observe(this){
                                setSharedPrefsLatAndLon(it)
                                homeFragmentViewModel.fetchingWeather(it.latitude,it.longitude,language, units)
                            }
                        }else {
                            try {
                                locationHelper.requestPermission()
                            }finally {
                                homeFragmentViewModel.getFreshLocation()
                                homeFragmentViewModel.getLastLocation().observe(this){
                                    setSharedPrefsLatAndLon(it)
                                    homeFragmentViewModel.fetchingWeather(it.latitude,it.longitude,language, units)
                                }
                            }
                        }
                    }
                }
            }

        }else {
            homeFragmentViewModel.getWeatherFromDataBase()
        }

        homeFragmentViewModel.returnWeather().observe(this.viewLifecycleOwner) { weather ->
            setData(weather)
        }
    }

    private fun createViewModel() {
        homeFragmentViewModelFactory = HomeFragmentViewModelFactory(
            Repositry.getInstance(
                RemoteDataSource.getInstance(),
                LocalDataSource.getInstance(requireContext()),
                requireContext()),
            locationHelper
        )

        homeFragmentViewModel = ViewModelProvider(this,homeFragmentViewModelFactory)[HomeFragmentViewModel::class.java]

    }

    private fun initUI(view: View) {
        val hourlyRecyclerview = view.findViewById<RecyclerView>(R.id.hourlyTempRecyclerView)
        hourlyRecyclerview.layoutManager = LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)
        hourlyRecyclerViewAdapter = HourlyRecyclerViewAdapter()
        hourlyRecyclerview.adapter = hourlyRecyclerViewAdapter

        val dailyRecyclerview = view.findViewById<RecyclerView>(R.id.dailyTempRecyclerView)
        dailyRecyclerview.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
        dailyRecyclerViewAdapter = DailyRecyclerViewAdapter()
        dailyRecyclerViewAdapter.setValuesFromSharedPreferences(requireContext())
        dailyRecyclerview.adapter = dailyRecyclerViewAdapter

        tvCityName = view.findViewById(R.id.tvHomeCityName)
        tvCurrentTemp = view.findViewById(R.id.tvHomeTemperature)
        tvWeatherState = view.findViewById(R.id.tvWeatherState)
        tvCurrentMaxTemp = view.findViewById(R.id.tvCurrentMaxTemp)
        tvCurrentMinTemp = view.findViewById(R.id.tvCurrentMinTemp)

        tvCloud = view.findViewById(R.id.tvCloud)
        tvCloudValue = view.findViewById(R.id.tvCloudValue)
        ivCloud = view.findViewById(R.id.imageViewCloud)

        tvPressure = view.findViewById(R.id.tvPressure)
        tvPressureValue = view.findViewById(R.id.tvPressureValue)
        ivPressure = view.findViewById(R.id.imageViewPressure)

        tvWind = view.findViewById(R.id.tvWind)
        tvWindValue = view.findViewById(R.id.tvWindValue)
        ivWind = view.findViewById(R.id.imageViewWind)

        tvHumidity = view.findViewById(R.id.tvHumidity)
        tvHumidityValue = view.findViewById(R.id.tvHumidityValue)
        ivHumidity = view.findViewById(R.id.imageViewHumidity)

        tvVisibility = view.findViewById(R.id.tvVisibility)
        tvVisibilityValue = view.findViewById(R.id.tvVisibilityValue)
        ivVisibility = view.findViewById(R.id.imageViewVisibility)

        tvUltraViolet = view.findViewById(R.id.tvUltraViolet)
        tvUltraVioletValue = view.findViewById(R.id.tvUltraVioletValue)
        ivUltraViolet = view.findViewById(R.id.imageViewUltraViolet)
    }

    @SuppressLint("SetTextI18n")
    private fun setData(weather: WeatherApi) {

        val language = getSharedPreferences(requireContext()).getString("languageSetting","en")!!
        val units = getSharedPreferences(requireContext()).getString(getString(R.string.unitsSetting),"metric")!!

        when(language) {
            "en" -> {
                setEnglishUnits("metric")
                tvCurrentTemp.text = "${weather.current.temp.toInt()}$tempeUnit"
                tvCurrentMaxTemp.text = "H:${weather.daily[0].temp.max.toInt()}$tempeUnit"
                tvCurrentMinTemp.text = "L:${weather.daily[0].temp.min.toInt()}$tempeUnit"
                tvCloudValue.text = "${weather.current.clouds} hps"
                tvHumidityValue.text = "${weather.current.humidity}%"
                tvPressureValue.text = "${weather.current.pressure} hps"
                tvVisibilityValue.text = "${weather.current.visibility}$windSpeedUnit"
                tvUltraVioletValue.text = "${weather.current.uvi.toInt()}%"
                tvWindValue.text = "${weather.current.windSpeed}$windSpeedUnit"
            }

            "ar" -> {
                setArabicUnit(units)
                tvCurrentTemp.text = "${convertNumbersToArabic(weather.current.temp.toInt())}$tempeUnit"
                tvCurrentMaxTemp.text = "ع: ${convertNumbersToArabic(weather.daily[0].temp.max.toInt())}${tempeUnit}"
                tvCurrentMinTemp.text = "ص: ${convertNumbersToArabic(weather.daily[0].temp.min.toInt())}${tempeUnit}"
                tvPressureValue.text = "${convertNumbersToArabic(weather.current.pressure)}هب"
                tvHumidityValue.text = "${convertNumbersToArabic(weather.current.humidity)}٪"
                tvWindValue.text = "${convertNumbersToArabic(weather.current.windSpeed)}$windSpeedUnit"
                tvCloudValue.text = "${convertNumbersToArabic(weather.current.clouds)}هب"
                tvUltraVioletValue.text = "${convertNumbersToArabic(weather.current.uvi.toInt())}٪"
                tvVisibilityValue.text = "${convertNumbersToArabic(weather.current.visibility)}$windSpeedUnit"
            }
        }


        tvCityName.text = weather.timezone
        tvWeatherState.text = weather.current.weather[0].description

        dailyRecyclerViewAdapter.listOfDailyTemps = weather.daily
        dailyRecyclerViewAdapter.notifyDataSetChanged()
        hourlyRecyclerViewAdapter.listOfHourlyTemps = weather.hourly
        hourlyRecyclerViewAdapter.notifyDataSetChanged()

    }

    private fun setArabicUnit(units: String) {
        when (units) {
            "metric" -> {
                tempeUnit = " °س"
                windSpeedUnit = " م/ث"
            }
            "imperial" -> {
                tempeUnit = " °ف"
                windSpeedUnit = " ميل/س"
            }
            "standard" -> {
                tempeUnit = " °ك"
                windSpeedUnit = " م/ث"
            }
        }
    }

    private fun setEnglishUnits(units: String) {
        when (units) {
            "metric" -> {
                tempeUnit = "°C"
                windSpeedUnit = "m/s"
            }
            "imperial" -> {
                tempeUnit = "°F"
                windSpeedUnit = "miles/h"
            }
            "standard" -> {
                tempeUnit = "°K"
                windSpeedUnit = "m/s"
            }
        }
    }

    private fun setSharedPrefsLatAndLon(location: Location) {
        val editor = getSharedPreferences(requireContext()).edit()
        editor.putFloat("lat", location.latitude.toFloat())
        editor.putFloat("lon", location.longitude.toFloat())
        editor.commit()
    }

    private fun navigateToMap() {
        val intent = Intent(requireContext(), MapActivity::class.java)
        intent.putExtra("fromFavouriteScreen",false)
        startActivity(intent)
    }

}