package com.iti.mad42.weatherforcast.favourites.favouritesRecyclerView.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iti.mad42.weatherforcast.R
import com.iti.mad42.weatherforcast.model.FavouritePlace
import com.iti.mad42.weatherforcast.model.Hourly

class FavouritePlacesRecyclerViewAdapter(private val context : Context,
private val onClickDeleteListener: OnClickDeleteListener,
private val onClickNavigateListener: OnClickNavigateListener) : RecyclerView.Adapter<FavouritePlacesRecyclerViewAdapter.MyViewHolder>() {

    var listOfFavouritePlaces: List<FavouritePlace> = arrayListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.favourite_place_row, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavouritePlacesRecyclerViewAdapter.MyViewHolder, position: Int) {
        holder.tvPlace.text = listOfFavouritePlaces[position].name
        holder.btnDelete.setOnClickListener {
            onClickDeleteListener.onClickDeleteListener(
                listOfFavouritePlaces[position]
            )
        }
        holder.btnNavigateToWeather.setOnClickListener {
            onClickNavigateListener.onClickNavigateListener(
                listOfFavouritePlaces[position]
            )
        }
    }

    override fun getItemCount(): Int {
        return listOfFavouritePlaces.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPlace: TextView
        val btnDelete : Button
        val btnNavigateToWeather : Button

        init {
            tvPlace = view.findViewById(R.id.tvPlace)
            btnDelete = view.findViewById(R.id.btnDelete)
            btnNavigateToWeather = view.findViewById(R.id.btnNavigateToWeather)
        }
    }
}