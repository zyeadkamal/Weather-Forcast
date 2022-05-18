package com.iti.mad42.weatherforcast.alerts.alertsRecyclerView.view

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.iti.mad42.weatherforcast.R
import com.iti.mad42.weatherforcast.alerts.addAlert.view.AddAlertActivity
import com.iti.mad42.weatherforcast.alerts.alertsRecyclerView.viewModel.AlertsViewModel
import com.iti.mad42.weatherforcast.alerts.alertsRecyclerView.viewModel.AlertsViewModelFactory
import com.iti.mad42.weatherforcast.alerts.alertsRecyclerView.viewModel.AlertsViewModelInterface
import com.iti.mad42.weatherforcast.model.Repositry.Repositry
import com.iti.mad42.weatherforcast.model.WeatherAlerts
import com.iti.mad42.weatherforcast.model.db.LocalDataSource
import com.iti.mad42.weatherforcast.model.network.RemoteDataSource


class AlertsFragment : Fragment(),OnClickDeleteAlertListener {


    private lateinit var alertViewModel : AlertsViewModelInterface
    private lateinit var alertsViewModelFactory: AlertsViewModelFactory

    private lateinit var alertsRV : RecyclerView
    private lateinit var alertsRecyclerViewAdapter: AlertsRecyclerViewAdapter
    private lateinit var btnAddAlert : FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alerts, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI(view)
        createViewModel()
        setClickListeners()
        getAllAlerts()
    }


    private fun initUI(view : View) {

        btnAddAlert = view.findViewById(R.id.btnAddAlert)
        alertsRV = view.findViewById(R.id.recyclerViewAlerts)
        alertsRecyclerViewAdapter = AlertsRecyclerViewAdapter(this)
        val alertLayoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
        alertsRV.layoutManager = alertLayoutManager
        alertsRV.adapter = alertsRecyclerViewAdapter
    }

    private fun createViewModel() {
        alertsViewModelFactory = AlertsViewModelFactory(Repositry.getInstance(RemoteDataSource.getInstance(),LocalDataSource.getInstance(requireContext()),requireContext()))
        alertViewModel = ViewModelProvider(this,alertsViewModelFactory)[AlertsViewModel::class.java]
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setClickListeners() {
        btnAddAlert.setOnClickListener {
            navigateToAddAlertActivity()
        }
    }

    private fun getAllAlerts(){
        alertViewModel.getAllAlerts().observe(this){
            if (it !=null)
                alertsRecyclerViewAdapter.listOfAlerts = it
                alertsRecyclerViewAdapter.setValuesFromSharedPreferences(requireContext())
                alertsRecyclerViewAdapter.notifyDataSetChanged()
        }
    }

    override fun deleteAlert(weatherAlerts: WeatherAlerts) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.areYouSure))
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                alertViewModel.deleteAlert(weatherAlerts)
                dialog.dismiss()
                Toast.makeText(requireContext(),getString(R.string.deletedSuccessfully), Toast.LENGTH_SHORT).show()
            }.show()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun navigateToAddAlertActivity(){
        try {
            checkDrawOverlayPermission()
        }finally {
            startActivity(Intent(requireContext(), AddAlertActivity::class.java))
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkDrawOverlayPermission() {
        // Check if we already  have permission to draw over other apps
        if (!Settings.canDrawOverlays(requireContext())) {
            // if not construct intent to request permission
            val alertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
            alertDialogBuilder.setTitle(getString(R.string.overlay_title))
                .setMessage(getString(R.string.overlay_message))
                .setPositiveButton(getString(R.string.overlay_postive_button)) { dialog: DialogInterface, _: Int ->
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + requireContext().packageName)
                    )
                    dialog.dismiss()
                    // request permission via start activity for result
                    startActivityForResult(intent, 1)
                    //It will call onActivityResult Function After you press Yes/No and go Back after giving permission
                }.setNegativeButton(
                    getString(R.string.overlay_negative_button)
                ) { dialog: DialogInterface, _: Int ->
                    dialog.dismiss()
                }.show()
        }
    }


}