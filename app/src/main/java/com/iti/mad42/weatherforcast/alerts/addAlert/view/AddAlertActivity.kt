package com.iti.mad42.weatherforcast.alerts.addAlert.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.google.android.material.textfield.TextInputLayout
import com.iti.mad42.weatherforcast.R
import com.iti.mad42.weatherforcast.Utilities.convertLongToTime
import com.iti.mad42.weatherforcast.Utilities.dateToLong
import com.iti.mad42.weatherforcast.Utilities.getDays
import com.iti.mad42.weatherforcast.Utilities.timeToSeconds
import com.iti.mad42.weatherforcast.alerts.addAlert.viewModel.AddAlertViewModel
import com.iti.mad42.weatherforcast.alerts.addAlert.viewModel.AddAlertViewModelFactory
import com.iti.mad42.weatherforcast.alerts.addAlert.viewModel.AddAlertViewModelInterface
import com.iti.mad42.weatherforcast.model.Repositry.Repositry
import com.iti.mad42.weatherforcast.model.db.LocalDataSource
import com.iti.mad42.weatherforcast.model.network.RemoteDataSource
import com.iti.mad42.weatherforcast.workManger.MyPeriodicManager
import java.util.*
import java.util.concurrent.TimeUnit

class AddAlertActivity : AppCompatActivity() {

    private lateinit var addAlertViewModel: AddAlertViewModelInterface
    private lateinit var addAlertViewModelFactory: AddAlertViewModelFactory

    private lateinit var rgAlertType: RadioGroup
    private lateinit var btnTypeAlarm: RadioButton
    private lateinit var btnTypeNotification: RadioButton
    private lateinit var btnSave: Button
    private lateinit var etAlertFromDate: TextInputLayout
    private lateinit var etAlertToDate: TextInputLayout
    private lateinit var etAlertTime: TextInputLayout
    private lateinit var btnClose: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_alert)

        initUI()
        createViewModel()
        setClickListeners()
    }

    private fun initUI() {
        rgAlertType = findViewById(R.id.radioGroupAlertType)
        btnTypeAlarm = findViewById(R.id.btnTypeAlarm)
        btnTypeNotification = findViewById(R.id.btnTypeNotification)
        btnSave = findViewById(R.id.btnSaveAlert)
        etAlertFromDate = findViewById(R.id.etAlertFromDate)
        etAlertToDate = findViewById(R.id.etAlertToDate)
        etAlertTime = findViewById(R.id.etAlertTime)
        btnClose = findViewById(R.id.btnCloseAddAlert)
    }

    private fun createViewModel() {
        addAlertViewModelFactory = AddAlertViewModelFactory(Repositry.getInstance(RemoteDataSource.getInstance(),LocalDataSource.getInstance(this),this))
        addAlertViewModel = ViewModelProvider(this,addAlertViewModelFactory)[AddAlertViewModel::class.java]
    }

    private fun setClickListeners() {
        btnClose.setOnClickListener{
            finish()
        }

        etAlertTime.editText?.setOnClickListener {
            openTimePicker()
        }

        etAlertFromDate.editText?.setOnClickListener {
            openDatePicker("from")
        }

        etAlertToDate.editText?.setOnClickListener {
            openDatePicker("to")
        }

        btnSave.setOnClickListener {
            getDataFromDialog()
        }
    }


    private fun openDatePicker(state: String) {
        val c = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                if (state == "from") {
                    addAlertViewModel.assignStartDateString(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    Log.e("mando", "openDatePicker: "+addAlertViewModel.returnStartDateString())
                    addAlertViewModel.assignStartDate(dateToLong(addAlertViewModel.returnStartDateString()))
                    etAlertFromDate.editText?.setText(addAlertViewModel.returnStartDateString())
                } else {
                    addAlertViewModel.assignEndDateString(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    addAlertViewModel.assignEndDate(dateToLong(addAlertViewModel.returnEndDateString()))
                    etAlertToDate.editText?.setText(addAlertViewModel.returnEndDateString())
                }
            }, c[Calendar.YEAR], c[Calendar.MONTH], c[Calendar.DAY_OF_MONTH]
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }


    private fun openTimePicker() {
        val calendar = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(this,
            { viewTimePicker, hour, minute ->
                addAlertViewModel.assignAlertTime(timeToSeconds(hour, minute))
                etAlertTime.editText?.setText(convertLongToTime(addAlertViewModel.returnAlertTime(),addAlertViewModel.returnLanguage()))
            }, calendar[Calendar.HOUR_OF_DAY], calendar[Calendar.MINUTE], true
        )
        timePickerDialog.show()
    }

    private fun getDataFromDialog(){
        if (btnTypeAlarm.isChecked || btnTypeNotification.isChecked && !etAlertFromDate.editText?.text.isNullOrBlank() && !etAlertToDate.editText?.text.isNullOrBlank() && !etAlertTime.editText?.text.isNullOrBlank()){
            when (rgAlertType.checkedRadioButtonId) {
                R.id.btnTypeNotification -> {
                    addAlertViewModel.assignAlertType("notification")
                }
                R.id.btnTypeAlarm -> {
                    addAlertViewModel.assignAlertType("alarm")
                }
            }
            addAlertViewModel.assignAlertDays(getDays(addAlertViewModel.returnStartDateString(),addAlertViewModel.returnEndDateString()))

            addAlertViewModel.insertAlert()
            setPeriodWorkManger()
            finish()
        }
        else
        {
            Toast.makeText(this, getString(R.string.fillRequired), Toast.LENGTH_SHORT).show()
        }
    }

    private fun setPeriodWorkManger() {

        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val periodicWorkRequest = PeriodicWorkRequest.Builder(
            MyPeriodicManager::class.java, 24, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            "work", ExistingPeriodicWorkPolicy.REPLACE, periodicWorkRequest)
        Log.e("Create alarm","setPeriodWorkManger")

    }


}