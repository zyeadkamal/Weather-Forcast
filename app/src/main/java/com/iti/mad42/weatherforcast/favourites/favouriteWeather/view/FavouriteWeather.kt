package com.iti.mad42.weatherforcast.favourites.favouriteWeather.view

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iti.mad42.weatherforcast.R
import com.iti.mad42.weatherforcast.Utilities.Utilities
import com.iti.mad42.weatherforcast.Utilities.convertNumbersToArabic
import com.iti.mad42.weatherforcast.Utilities.getCurrentLocale
import com.iti.mad42.weatherforcast.favourites.favouriteWeather.viewModel.FavouriteWeatherViewModel
import com.iti.mad42.weatherforcast.favourites.favouriteWeather.viewModel.FavouriteWeatherViewModelFactory
import com.iti.mad42.weatherforcast.favourites.favouriteWeather.viewModel.FavouriteWeatherViewModelInterface
import com.iti.mad42.weatherforcast.home.view.DailyRecyclerViewAdapter
import com.iti.mad42.weatherforcast.home.view.HourlyRecyclerViewAdapter
import com.iti.mad42.weatherforcast.model.Repositry.Repositry
import com.iti.mad42.weatherforcast.model.WeatherApi
import com.iti.mad42.weatherforcast.model.db.LocalDataSource
import com.iti.mad42.weatherforcast.model.network.RemoteDataSource
import com.iti.mad42.weatherforcast.Utilities.Utilities.Companion.tempeUnit
import com.iti.mad42.weatherforcast.Utilities.Utilities.Companion.windSpeedUnit

class FavouriteWeather : AppCompatActivity() {


    private lateinit var favouriteWeatherViewModel: FavouriteWeatherViewModelInterface
    private lateinit var favouriteWeatherViewModelFactory: FavouriteWeatherViewModelFactory

    private lateinit var btnClose : Button
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite_weather)

        initUI()
        createViewModel()
        setClickListeners()
        try {
            fetchWeather()
        }finally {
            observeWeather()
        }

    }


    private fun initUI() {
        val hourlyRecyclerview = findViewById<RecyclerView>(R.id.hourlyTempFavouriteRecyclerView)
        hourlyRecyclerview.layoutManager = LinearLayoutManager(this,
            RecyclerView.HORIZONTAL,false)
        hourlyRecyclerViewAdapter = HourlyRecyclerViewAdapter()
        hourlyRecyclerview.adapter = hourlyRecyclerViewAdapter

        val dailyRecyclerview = findViewById<RecyclerView>(R.id.dailyTempFavouriteRecyclerView)
        dailyRecyclerview.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)
        dailyRecyclerViewAdapter = DailyRecyclerViewAdapter()
        dailyRecyclerview.adapter = dailyRecyclerViewAdapter

        btnClose = findViewById(R.id.btnCloseActivity)

        tvCityName = findViewById(R.id.tvFavouriteCityName)
        tvCurrentTemp = findViewById(R.id.tvFavouriteTemperature)
        tvWeatherState = findViewById(R.id.tvFavouriteWeatherState)
        tvCurrentMaxTemp = findViewById(R.id.tvFavouriteCurrentMaxTemp)
        tvCurrentMinTemp = findViewById(R.id.tvFavouriteCurrentMinTemp)

        tvCloud = findViewById(R.id.tvCloudFavourite)
        tvCloudValue = findViewById(R.id.tvCloudValueFavourite)
        ivCloud = findViewById(R.id.imageViewCloudFavourite)

        tvPressure = findViewById(R.id.tvPressureFavourite)
        tvPressureValue = findViewById(R.id.tvPressureValueFavourite)
        ivPressure = findViewById(R.id.imageViewPressureFavourite)

        tvWind = findViewById(R.id.tvWindFavourite)
        tvWindValue = findViewById(R.id.tvWindValueFavourite)
        ivWind = findViewById(R.id.imageViewWindFavourite)

        tvHumidity = findViewById(R.id.tvHumidityFavourite)
        tvHumidityValue = findViewById(R.id.tvHumidityValueFavourite)
        ivHumidity = findViewById(R.id.imageViewHumidityFavourite)

        tvVisibility = findViewById(R.id.tvVisibilityFavourite)
        tvVisibilityValue = findViewById(R.id.tvVisibilityValueFavourite)
        ivVisibility = findViewById(R.id.imageViewVisibilityFavourite)

        tvUltraViolet = findViewById(R.id.tvUltraVioletFavourite)
        tvUltraVioletValue = findViewById(R.id.tvUltraVioletValueFavourite)
        ivUltraViolet = findViewById(R.id.imageViewUltraVioletFavourite)
    }

    private fun setClickListeners() {
        btnClose.setOnClickListener({ finish() })
    }

    private fun createViewModel() {
        favouriteWeatherViewModelFactory = FavouriteWeatherViewModelFactory(Repositry.getInstance(
            RemoteDataSource.getInstance(), LocalDataSource(this), this)
        )

        favouriteWeatherViewModel = ViewModelProvider(this,favouriteWeatherViewModelFactory)[FavouriteWeatherViewModel::class.java]
    }

    @SuppressLint("SetTextI18n")
    private fun setData(weather: WeatherApi) {

        val language = com.iti.mad42.weatherforcast.Utilities.getSharedPreferences(this)
            .getString(getString(R.string.languageSetting),"en")!!
        val units = com.iti.mad42.weatherforcast.Utilities.getSharedPreferences(this)
            .getString(getString(R.string.unitsSetting),"metric")!!

        when(language) {
            "en" -> {
                setEnglishUnits("metric")
                tvCurrentTemp.text = "${weather.current.temp.toInt()}${Utilities.tempeUnit}"
                tvCurrentMaxTemp.text = "H:${weather.daily[0].temp.max.toInt()}${Utilities.tempeUnit}"
                tvCurrentMinTemp.text = "L:${weather.daily[0].temp.min.toInt()}${Utilities.tempeUnit}"
                tvCloudValue.text = "${weather.current.clouds} hps"
                tvHumidityValue.text = "${weather.current.humidity}%"
                tvPressureValue.text = "${weather.current.pressure} hps"
                tvVisibilityValue.text = "${weather.current.visibility}${Utilities.windSpeedUnit}"
                tvUltraVioletValue.text = "${weather.current.uvi.toInt()}%"
                tvWindValue.text = "${weather.current.windSpeed}${Utilities.windSpeedUnit}"
            }

            "ar" -> {
                setArabicUnit(units)
                tvCurrentTemp.text = "${convertNumbersToArabic(weather.current.temp.toInt())}${Utilities.tempeUnit}"
                tvCurrentMaxTemp.text = "ع: ${convertNumbersToArabic(weather.daily[0].temp.max.toInt())}${Utilities.tempeUnit}"
                tvCurrentMinTemp.text = "ص: ${convertNumbersToArabic(weather.daily[0].temp.min.toInt())}${Utilities.tempeUnit}"
                tvPressureValue.text = "${convertNumbersToArabic(weather.current.pressure)}هب"
                tvHumidityValue.text = "${convertNumbersToArabic(weather.current.humidity)}٪"
                tvWindValue.text = "${convertNumbersToArabic(weather.current.windSpeed)}${Utilities.windSpeedUnit}"
                tvCloudValue.text = "${convertNumbersToArabic(weather.current.clouds)}هب"
                tvUltraVioletValue.text = "${convertNumbersToArabic(weather.current.uvi.toInt())}٪"
                tvVisibilityValue.text = "${convertNumbersToArabic(weather.current.visibility)}${Utilities.windSpeedUnit}"
            }
        }


        tvCityName.text = weather.timezone
        tvWeatherState.text = weather.current.weather[0].description

        dailyRecyclerViewAdapter.listOfDailyTemps = weather.daily
        dailyRecyclerViewAdapter.setValuesFromSharedPreferences(this)
        dailyRecyclerViewAdapter.notifyDataSetChanged()
        hourlyRecyclerViewAdapter.listOfHourlyTemps = weather.hourly
        hourlyRecyclerViewAdapter.notifyDataSetChanged()



    }

    private fun setArabicUnit(units: String) {
        when (units) {
            "metric" -> {
                tempeUnit = " °م"
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

    private fun fetchWeather() {
        val local = getCurrentLocale(this)
        val language = com.iti.mad42.weatherforcast.Utilities.getSharedPreferences(this)
            .getString(getString(R.string.languageSetting),
            local?.language
        )!!
        val units = com.iti.mad42.weatherforcast.Utilities.getSharedPreferences(this)
            .getString(getString(R.string.unitsSetting),
            "metric"
        )!!
        favouriteWeatherViewModel.fetchingWeather(intent.getDoubleExtra("lat",31.037933),
            intent.getDoubleExtra("lon",31.381523),
            language,
            units)
    }

    private fun observeWeather() {
        favouriteWeatherViewModel.returnWeather().observe(this) {
            setData(it)
        }
    }
}