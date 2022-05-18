package com.iti.mad42.weatherforcast.alerts.alertsRecyclerView.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iti.mad42.weatherforcast.R
import com.iti.mad42.weatherforcast.Utilities.convertLongToTime
import com.iti.mad42.weatherforcast.Utilities.getSharedPreferences
import com.iti.mad42.weatherforcast.Utilities.longToDateAsString
import com.iti.mad42.weatherforcast.model.WeatherAlerts

class AlertsRecyclerViewAdapter(private var onClickDeleteAlertListener: OnClickDeleteAlertListener) : RecyclerView.Adapter<AlertsRecyclerViewAdapter.MyViewHolder>() {

    var listOfAlerts: List<WeatherAlerts> = arrayListOf()
    private var language : String = "en"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.alert_recyclerview_row, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlertsRecyclerViewAdapter.MyViewHolder, position: Int) {
        holder.tvStartDate.text = longToDateAsString(listOfAlerts[position].startDate,language)
        holder.tvStartTime.text = convertLongToTime(listOfAlerts[position].alertTime,language)
        holder.tvEndDate.text = longToDateAsString(listOfAlerts[position].endDate,language)
        holder.tvEndTime.text = convertLongToTime(listOfAlerts[position].alertTime,language)
        holder.btnDelete.setOnClickListener({ onClickDeleteAlertListener.deleteAlert(listOfAlerts[position]) })
    }

    override fun getItemCount(): Int {
        return listOfAlerts.size
    }

    fun setValuesFromSharedPreferences(context: Context) {
        getSharedPreferences(context).apply {
            language = getString(context.getString(R.string.languageSetting), "en") ?: "en"
        }
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvStartTime: TextView = view.findViewById(R.id.tvStartTime)
        val tvEndTime: TextView = view.findViewById(R.id.tvEndTime)
        val tvStartDate : TextView = view.findViewById(R.id.tvStartDate)
        val tvEndDate : TextView = view.findViewById(R.id.tvEndDate)
        val btnDelete : Button = view.findViewById(R.id.btnDeleteAlert)

    }
}