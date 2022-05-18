package com.iti.mad42.weatherforcast.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iti.mad42.weatherforcast.R
import com.iti.mad42.weatherforcast.Utilities.convertLongToTime
import com.iti.mad42.weatherforcast.Utilities.getSharedPreferences
import com.iti.mad42.weatherforcast.model.Hourly

class HourlyRecyclerViewAdapter : RecyclerView.Adapter<HourlyRecyclerViewAdapter.MyViewHolder>() {

    var listOfHourlyTemps: List<Hourly> = arrayListOf()
    private var language: String = "en"
    private var units: String = "metric"

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.hourly_recyclerview_row, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: HourlyRecyclerViewAdapter.MyViewHolder, position: Int) {
        holder.tvTime.text = convertLongToTime(listOfHourlyTemps[position].dt,language)
        holder.tvTemp.text = "${listOfHourlyTemps[position].temp.toInt()}°C"
    }

    override fun getItemCount(): Int {
        return listOfHourlyTemps.size
    }

    fun setValuesFromSharedPreferences(context: Context) {
        getSharedPreferences(context).apply {
            language = getString("languageSetting", "en") ?: "en"
            units = getString(context.getString(R.string.unitsSetting), "metric") ?: "metric"
        }
    }


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTime: TextView
        val tvTemp : TextView

        init {
            // Define click listener for the ViewHolder's View.
            tvTime = view.findViewById(R.id.tvTimeOfHourlyRV)
            tvTemp = view.findViewById(R.id.tvTempOfHourlyRV)
        }
    }
}

