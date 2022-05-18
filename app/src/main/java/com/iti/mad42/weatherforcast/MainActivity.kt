package com.iti.mad42.weatherforcast

import android.content.res.Configuration
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.iti.mad42.weatherforcast.Utilities.getCurrentLocale
import com.iti.mad42.weatherforcast.Utilities.getSharedPreferences
import com.iti.mad42.weatherforcast.alerts.alertsRecyclerView.view.AlertsFragment
import com.iti.mad42.weatherforcast.favourites.favouritesRecyclerView.view.FavouritesFragment
import com.iti.mad42.weatherforcast.home.view.HomeFragment
import com.iti.mad42.weatherforcast.settings.view.SettingsFragment
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var bottomNavigation : BottomNavigationView
    private val homeFragment = HomeFragment()
    private val alertsFragment = AlertsFragment()
    private val favouritesFragment = FavouritesFragment()
    private val settingsFragment = SettingsFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigation = findViewById(R.id.bottomNavigation)
        replaceFragments(homeFragment)
        setNavigationItemListeners()

        val localLang = getCurrentLocale(this)
        val languageLocale = getSharedPreferences(this).getString(
            getString(R.string.languageSetting), localLang?.language) ?: localLang?.language
        setLocale(languageLocale!!)

    }

    private fun replaceFragments(fragment: Fragment) {
        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frahmentContainer,fragment)
            transaction.commit()
        }
    }

    private fun setNavigationItemListeners() {
        bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.homeButton -> replaceFragments(homeFragment)
                R.id.favouritesButton -> replaceFragments(favouritesFragment)
                R.id.alertButton -> replaceFragments(alertsFragment)
                R.id.settingsButton -> replaceFragments(settingsFragment)
            }
            true
        }
    }

    private fun setLocale(lang: String) {
        val myLocale = Locale(lang)
        Locale.setDefault(myLocale)
        val res: Resources = resources
        val dm: DisplayMetrics = res.displayMetrics
        val conf: Configuration = res.configuration
        conf.locale = myLocale
        conf.setLayoutDirection(myLocale)
        res.updateConfiguration(conf, dm)
    }
}