package com.iti.mad42.weatherforcast.home.view

import android.content.Context
import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iti.mad42.weatherforcast.R
import com.iti.mad42.weatherforcast.Utilities.getDayOfWeek
import com.iti.mad42.weatherforcast.Utilities.getIcon
import com.iti.mad42.weatherforcast.Utilities.getSharedPreferences
import com.iti.mad42.weatherforcast.model.Daily

class DailyRecyclerViewAdapter : RecyclerView.Adapter<DailyRecyclerViewAdapter.MyViewHolder>() {

    var listOfDailyTemps: List<Daily> = arrayListOf()
    private var language: String = "en"
    private var units: String = "metric"

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.daily_recyclerview_row, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: DailyRecyclerViewAdapter.MyViewHolder, position: Int) {

        holder.tvDayName.text = getDayOfWeek(listOfDailyTemps[position].dt,language)
        holder.tvTemp.text = "${listOfDailyTemps[position].temp.max.toInt()}°C/${listOfDailyTemps[position].temp.min.toInt()}°C"
        holder.tempIV.setImageResource(getIcon(listOfDailyTemps[position].weather[0].icon))
    }

    fun setValuesFromSharedPreferences(context: Context) {
        getSharedPreferences(context).apply {
            language = getString("languageSetting", "en") ?: "en"
            units = getString(context.getString(R.string.unitsSetting), "metric") ?: "metric"
        }
    }

    override fun getItemCount(): Int {
        return listOfDailyTemps.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDayName : TextView
        val tvTemp : TextView
        val tempIV : ImageView

        init {
            // Define click listener for the ViewHolder's View.
            tvDayName = view.findViewById(R.id.dailyRVDayName)
            tvTemp = view.findViewById(R.id.dailyRVTempTV)
            tempIV = view.findViewById(R.id.dailyRVTempImageView)
        }
    }
}