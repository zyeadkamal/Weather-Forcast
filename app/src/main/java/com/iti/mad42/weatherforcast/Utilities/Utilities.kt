package com.iti.mad42.weatherforcast.Utilities

import android.content.Context
import android.content.SharedPreferences
import android.location.Geocoder
import android.os.Build
import com.iti.mad42.weatherforcast.R
import org.joda.time.Days
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import java.io.IOException
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class Utilities {
    companion object{
        var windSpeedUnit: String = ""
        var tempeUnit: String = ""
    }
}


fun getIcon(imageString: String): Int {
    val imageInInteger: Int
    when (imageString) {
        "01d" -> imageInInteger = R.drawable.icon_01d
        "01n" -> imageInInteger = R.drawable.icon_01n
        "02d" -> imageInInteger = R.drawable.icon_02d
        "02n" -> imageInInteger = R.drawable.icon_02n
        "03n" -> imageInInteger = R.drawable.icon_03n
        "03d" -> imageInInteger = R.drawable.icon_03d
        "04d" -> imageInInteger = R.drawable.icon_04d
        "04n" -> imageInInteger = R.drawable.icon_04n
        "09d" -> imageInInteger = R.drawable.icon_09d
        "09n" -> imageInInteger = R.drawable.icon_09n
        "10d" -> imageInInteger = R.drawable.icon_10d
        "10n" -> imageInInteger = R.drawable.icon_10n
        "11d" -> imageInInteger = R.drawable.icon_11d
        "11n" -> imageInInteger = R.drawable.icon_11n
        "13d" -> imageInInteger = R.drawable.icon_13d
        "13n" -> imageInInteger = R.drawable.icon_13n
        "50d" -> imageInInteger = R.drawable.icon_50d
        "50n" -> imageInInteger = R.drawable.icon_50n
        else -> imageInInteger = R.drawable.ic_weather_condition
    }
    return imageInInteger
}

fun getCityText(context: Context, lat: Double, lon: Double, language: String): String {
    var city = "Unknown Location"
    val geocoder = Geocoder(context, Locale(language))
    try {
        val addresses = geocoder.getFromLocation(lat, lon, 1)
        if (addresses.isNotEmpty()) {
            //Log.e("mando", "getCityText: ${addresses[0].featureName} , ${addresses[0].subAdminArea}  " )
            city = "${addresses[0].adminArea}"
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return city
}

fun dateToLong(date: String?): Long {
    val f = SimpleDateFormat("dd-MM-yyyy")
    var milliseconds: Long = 0
    try {
        val d = f.parse(date)
        milliseconds = d.time
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return milliseconds/1000
}

fun timeToSeconds(hour: Int, min: Int): Long {
    return (((hour * 60 + min) * 60) - 7200 ).toLong()
}

fun getCurrentDay(): String? {
    val dateFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy")
    val date = Date()
    return dateFormat.format(date)
}

fun convertLongToTime(time: Long, language: String = "en"): String {
    val date = Date(TimeUnit.SECONDS.toMillis(time))
    val format = SimpleDateFormat("h:mm a",  Locale(language))
    return format.format(date)
}
fun getDayOfWeek(timestamp: Long, language: String): String {
    return SimpleDateFormat("EEEE",  Locale(language)).format(timestamp * 1000)
}

fun longToDateAsString(dateInMillis: Long, language: String): String {
    val d = Date(dateInMillis * 1000)
    val dateFormat: DateFormat = SimpleDateFormat("d MMM, yyyy",  Locale(language))
    return dateFormat.format(d)
}

fun getCurrentTime(): Long {
    val hour =
        TimeUnit.HOURS.toMillis(Calendar.getInstance().get(Calendar.HOUR_OF_DAY).toLong())
    val minute =
        TimeUnit.MINUTES.toMillis(Calendar.getInstance().get(Calendar.MINUTE).toLong())

    return (hour + minute)
}

fun getDays(startDate: String, endDate: String): List<String> {
    val dtf = DateTimeFormat.forPattern("dd-MM-yyyy")
    val start = dtf.parseLocalDate(startDate)
    val end = dtf.parseLocalDate(endDate).plusDays(1)
    val myDays: MutableList<String> = ArrayList()
    val days = Days.daysBetween(LocalDate(start), LocalDate(end)).days.toLong()
    var i = 0
    while (i < days) {
        val current = start.plusDays(i)
        val date = current.toDateTimeAtStartOfDay().toString("dd-MM-yyyy")
        myDays.add(date)
        i++
    }
    return myDays
}

fun getSharedPreferences(context: Context): SharedPreferences {
    return context.getSharedPreferences(
        context.getString(R.string.sharedPrefs),
        Context.MODE_PRIVATE
    )
}

fun thereIsNoLatAndLon(context: Context): Boolean {
    val myPref = getSharedPreferences(context)
    val lat = myPref.getFloat("lat", 0.0f)
    val long = myPref.getFloat("lon", 0.0f)
    return lat == 0.0f && long == 0.0f
}

fun getLocationMethod(context: Context) : String {
    val myPref = getSharedPreferences(context)
    val method = myPref.getString("locationMethod", "null")
    return method?:"null"
}


fun getCurrentLocale(context: Context): Locale? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        context.resources.configuration.locales[0]
    } else {
        context.resources.configuration.locale
    }
}

fun convertNumbersToArabic(value: Double): String {
    return (value.toString() + "")
        .replace("1".toRegex(), "١").replace("2".toRegex(), "٢")
        .replace("3".toRegex(), "٣").replace("4".toRegex(), "٤")
        .replace("5".toRegex(), "٥").replace("6".toRegex(), "٦")
        .replace("7".toRegex(), "٧").replace("8".toRegex(), "٨")
        .replace("9".toRegex(), "٩").replace("0".toRegex(), "٠")
}

fun convertNumbersToArabic(value: Int): String {
    return (value.toString() + "")
        .replace("1".toRegex(), "١").replace("2".toRegex(), "٢")
        .replace("3".toRegex(), "٣").replace("4".toRegex(), "٤")
        .replace("5".toRegex(), "٥").replace("6".toRegex(), "٦")
        .replace("7".toRegex(), "٧").replace("8".toRegex(), "٨")
        .replace("9".toRegex(), "٩").replace("0".toRegex(), "٠")
}

