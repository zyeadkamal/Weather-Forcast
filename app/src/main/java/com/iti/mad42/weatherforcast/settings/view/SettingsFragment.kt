package com.iti.mad42.weatherforcast.settings.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import com.iti.mad42.weatherforcast.MainActivity
import com.iti.mad42.weatherforcast.R
import com.iti.mad42.weatherforcast.Utilities.getSharedPreferences
import java.util.*


class SettingsFragment : Fragment() {

    private lateinit var rgLocationMethod : RadioGroup
    private lateinit var rgLamguage : RadioGroup
    private lateinit var rgUnites : RadioGroup
    private lateinit var btnGPS : RadioButton
    private lateinit var btnMap : RadioButton
    private lateinit var btnArabic : RadioButton
    private lateinit var btnEnglish : RadioButton
    private lateinit var btnCelsius : RadioButton
    private lateinit var btnFahrenhite : RadioButton
    private lateinit var btnKelvin : RadioButton






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI(view)
        setOldValues()
        setClickListeners()

    }


    private fun initUI(view : View) {
        rgLocationMethod = view.findViewById(R.id.radioGroupLocationMethod)
        rgLamguage = view.findViewById(R.id.radioGroupLanguage)
        rgUnites = view.findViewById(R.id.radioGroupUnits)
        btnArabic = view.findViewById(R.id.radioBtnSettingArabic)
        btnEnglish = view.findViewById(R.id.radioBtnSettingEnglish)
        btnGPS = view.findViewById(R.id.radioBtnSettingGPS)
        btnMap = view.findViewById(R.id.radioBtnSettingMap)
        btnCelsius = view.findViewById(R.id.radioBtnSettingCelsius)
        btnFahrenhite = view.findViewById(R.id.radioBtnSettingFahrenheit)
        btnKelvin = view.findViewById(R.id.radioBtnSettingKelvin)

    }

    private fun setClickListeners() {

        rgLocationMethod.setOnCheckedChangeListener { radioGroup, i ->
            when(i){

                R.id.radioBtnSettingGPS -> {
                    val sharedPreferences = getSharedPreferences(requireContext())
                    val editor = sharedPreferences.edit()
                    editor.putString("locationMethod","gps")
                    editor.putFloat("lat", 0.0f)
                    editor.putFloat("lon", 0.0f)
                    editor.commit()
                }

                R.id.radioBtnSettingMap -> {
                    val sharedPreferences = getSharedPreferences(requireContext())
                    val editor = sharedPreferences.edit()
                    editor.putString("locationMethod","map")
                    editor.putFloat("lat", 0.0f)
                    editor.putFloat("lon", 0.0f)
                    editor.commit()
                }
            }
        }


        rgLamguage.setOnCheckedChangeListener { radioGroup, i ->
            when(i){

                R.id.radioBtnSettingArabic -> {


                    val sharedPreferences = getSharedPreferences(requireContext())
                    val editor = sharedPreferences.edit()
                    editor.putString(getString(R.string.languageSetting),"ar")
                    editor.commit()
                    navigateToHome()
                }

                R.id.radioBtnSettingEnglish -> {


                    val sharedPreferences = getSharedPreferences(requireContext())
                    val editor = sharedPreferences.edit()
                    editor.putString(getString(R.string.languageSetting),"en")
                    editor.commit()
                    navigateToHome()
                }
            }
        }


        rgUnites.setOnCheckedChangeListener { radioGroup, i ->
            when(i){

                R.id.radioBtnSettingCelsius -> {
                    val sharedPreferences = getSharedPreferences(requireContext())
                    val editor = sharedPreferences.edit()
                    editor.putString(getString(R.string.unitsSetting),"metric")
                    editor.commit()
                }

                R.id.radioBtnSettingFahrenheit -> {
                    val sharedPreferences = getSharedPreferences(requireContext())
                    val editor = sharedPreferences.edit()
                    editor.putString(getString(R.string.unitsSetting),"imperial")
                    editor.commit()
                }

                R.id.radioBtnSettingKelvin -> {
                    val sharedPreferences = getSharedPreferences(requireContext())
                    val editor = sharedPreferences.edit()
                    editor.putString(getString(R.string.unitsSetting),"standard")
                    editor.commit()
                }
            }
        }


    }


    private fun setOldValues() {
        val locationMethod = getSharedPreferences(requireContext()).getString("locationMethod","en")!!
        Log.e("Settings", "setOldValues:$locationMethod " )
        val language = getSharedPreferences(requireContext()).getString(getString(R.string.languageSetting),"en")!!
        Log.e("Settings", "setOldValues:$language " )
        val units = getSharedPreferences(requireContext()).getString(getString(R.string.unitsSetting),"metric")!!
        Log.e("Settings", "setOldValues:$units " )


        when(locationMethod) {
            "gps" -> {
                btnGPS.isChecked = true
            }
            "map" -> {
                btnMap.isChecked = true
            }
        }


        when(language) {
            "en" -> {
                btnEnglish.isChecked = true
            }
            "ar" -> {
                btnArabic.isChecked = true
            }
        }


        when(units) {
            "metric" -> {
                btnCelsius.isChecked = true
            }
            "imperial" -> {
                btnFahrenhite.isChecked = true
            }
            "standard" -> {
                btnKelvin.isChecked = true
            }
        }

    }

    private fun navigateToHome() {
        startActivity(Intent(requireContext(),MainActivity::class.java))
    }

}