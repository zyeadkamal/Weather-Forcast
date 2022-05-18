package com.iti.mad42.weatherforcast.favourites.favouritesRecyclerView.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.iti.mad42.weatherforcast.R
import com.iti.mad42.weatherforcast.Utilities.NetworkChangeReceiver.Companion.isAppOnline
import com.iti.mad42.weatherforcast.favourites.favouriteWeather.view.FavouriteWeather
import com.iti.mad42.weatherforcast.favourites.favouritesRecyclerView.viewModel.FavouritePlacesViewModel
import com.iti.mad42.weatherforcast.favourites.favouritesRecyclerView.viewModel.FavouritePlacesViewModelFactory
import com.iti.mad42.weatherforcast.favourites.favouritesRecyclerView.viewModel.FavouritePlacesViewModelInterface
import com.iti.mad42.weatherforcast.map.view.MapActivity
import com.iti.mad42.weatherforcast.model.FavouritePlace
import com.iti.mad42.weatherforcast.model.Repositry.Repositry
import com.iti.mad42.weatherforcast.model.db.LocalDataSource
import com.iti.mad42.weatherforcast.model.network.RemoteDataSource


class FavouritesFragment : Fragment(), OnClickDeleteListener, OnClickNavigateListener {


    private lateinit var placesRV : RecyclerView
    private lateinit var btnAddNewPlaace : FloatingActionButton
    private lateinit var favouritePlacesRecyclerViewAdapter : FavouritePlacesRecyclerViewAdapter
    private lateinit var favouritePlacesViewModelFactory: FavouritePlacesViewModelFactory
    private lateinit var favouritePlacesViewModel: FavouritePlacesViewModelInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI(view)
        setClickListeners()
        createViewModel()
        getAllFavouritePlaces()


    }

    private fun initUI(view: View) {

        placesRV = view.findViewById(R.id.recyclerViewFavouritePlaces)
        placesRV.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
        favouritePlacesRecyclerViewAdapter = FavouritePlacesRecyclerViewAdapter(requireContext(),this,this)
        placesRV.adapter = favouritePlacesRecyclerViewAdapter

        btnAddNewPlaace = view.findViewById(R.id.btnAddPlace)

    }

    private fun setClickListeners() {
        btnAddNewPlaace.setOnClickListener{
            if (isAppOnline){
                val intent = Intent(requireContext(), MapActivity::class.java)
                intent.putExtra("fromFavouriteScreen",true)
                startActivity(intent)
            }
            else{
                Toast.makeText(requireContext(), getString(R.string.pleaseCheckConnection), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createViewModel(){
        favouritePlacesViewModelFactory = FavouritePlacesViewModelFactory( Repositry.getInstance(
            RemoteDataSource.getInstance(), LocalDataSource(requireContext()), requireContext())
        )
        favouritePlacesViewModel = ViewModelProvider(this,favouritePlacesViewModelFactory)[FavouritePlacesViewModel::class.java]

    }

    private fun getAllFavouritePlaces() {
        favouritePlacesViewModel.getAllFavouritePlaces().observe(this){
            favouritePlacesRecyclerViewAdapter.listOfFavouritePlaces = it
            favouritePlacesRecyclerViewAdapter.notifyDataSetChanged()
        }
    }

    override fun onClickDeleteListener(place: FavouritePlace) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.areYouSure))
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                try {
                    favouritePlacesViewModel.deleteFavouritePlace(place)

                }finally {
                    getAllFavouritePlaces()
                    dialog.dismiss()
                    Toast.makeText(requireContext(),getString(R.string.deletedSuccessfully),Toast.LENGTH_SHORT).show()
                }
            }.show()
    }

    override fun onClickNavigateListener(place: FavouritePlace) {
        if (isAppOnline) {
            val intent = Intent(requireContext(), FavouriteWeather::class.java)
            intent.putExtra("lat",place.lat)
            intent.putExtra("lon",place.lon)
            intent.putExtra("title",place.name)
            startActivity(intent)
        }else {
            Toast.makeText(requireContext(),getString(R.string.pleaseCheckConnection),Toast.LENGTH_SHORT).show()
        }
    }


}