package com.iti.mad42.weatherforcast.splash

import android.app.Dialog
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.iti.mad42.weatherforcast.MainActivity
import com.iti.mad42.weatherforcast.R
import com.iti.mad42.weatherforcast.Utilities.NetworkChangeReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.*
import com.iti.mad42.weatherforcast.Utilities.getSharedPreferences
import com.iti.mad42.weatherforcast.map.view.MapActivity
import java.util.*

class SplashActivity : AppCompatActivity() {

    private lateinit var firstUseDialog: Dialog
    private lateinit var radioGroupLocation: RadioGroup
    private lateinit var btnGPS: RadioButton
    private lateinit var btnMap: RadioButton
    private lateinit var btnSave: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        startConnectivityChangeReceiver()

        val coroutineScope = CoroutineScope(Dispatchers.Main)
        coroutineScope.launch {
            delay(5000)

            if (isFirstUse()) {
                setFirstUseDone()
                openFirstUseDialog()
            }else {
                navigateToHome()
            }
        }
    }

    private fun navigateToHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startConnectivityChangeReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(NetworkChangeReceiver(this), intentFilter)
    }

    private fun isFirstUse() = getSharedPreferences(applicationContext).getBoolean("firstUse", true)

    private fun openFirstUseDialog() {

        firstUseDialog = Dialog(this)
        firstUseDialog.setContentView(R.layout.dialog_first_use)
        firstUseDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        radioGroupLocation = firstUseDialog.findViewById<RadioGroup>(R.id.radioGroupLocation)
        btnGPS = firstUseDialog.findViewById<RadioButton>(R.id.radioBtnGPS)
        btnMap = firstUseDialog.findViewById<RadioButton>(R.id.radioBtnMap)
        btnSave = firstUseDialog.findViewById<Button>(R.id.btnSaveLocationSetting)

        btnSave.setOnClickListener {

            if (btnGPS.isChecked || btnMap.isChecked){
                when (radioGroupLocation.checkedRadioButtonId) {
                    R.id.radioBtnMap -> {
                        saveLocationMethodInSharedPrefs("map")
                        navigateToMap()
                    }
                    R.id.radioBtnGPS -> {
                        saveLocationMethodInSharedPrefs("gps")
                        navigateToHome()
                    }
                }
                firstUseDialog.dismiss()
            }
            else
            {
                Toast.makeText(this, getString(R.string.chooseMethod), Toast.LENGTH_SHORT).show()
            }

        }
        firstUseDialog.show()
    }

    private fun setFirstUseDone() {
        val sharedPreferences = getSharedPreferences(this)
        val editor = sharedPreferences.edit()
        editor.putBoolean("firstUse",false)
        editor.commit()
    }

    private fun setLocale(lang: String) {
        val myLocale = Locale(lang)
        Locale.setDefault(myLocale)
        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.locale = myLocale
        conf.setLayoutDirection(myLocale)
        res.updateConfiguration(conf, dm)
    }

    private fun navigateToMap() {
        val intent = Intent(this, MapActivity::class.java)
        intent.putExtra("fromFavouriteScreen",false)
        startActivity(intent)
        finish()
    }


    private fun saveLocationMethodInSharedPrefs(value : String) {
        val sharedPreferences = getSharedPreferences(this)
        val editor = sharedPreferences.edit()
        editor.putString("locationMethod",value)
        editor.commit()
    }
}