package com.iti.mad42.weatherforcast.workManger


import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.iti.mad42.weatherforcast.R
import com.iti.mad42.weatherforcast.Utilities.getIcon


class AlertWindowManger (
    private val context: Context, private val description: String, private val icon: String)
{


    private lateinit var ivAlertDialog: ImageView
    private lateinit var tvDesc: TextView
    private lateinit var btnOk: Button

    private var windowManager: WindowManager? = null
    lateinit var customNotificationDialogView: View


    fun setMyWindowManger() {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        customNotificationDialogView = inflater.inflate(R.layout.dialog_alert, null)

        initView(customNotificationDialogView)

        val LAYOUT_FLAG: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }
        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val width = (context.resources.displayMetrics.widthPixels * 0.85).toInt()
        val params = WindowManager.LayoutParams(
            width,
            WindowManager.LayoutParams.WRAP_CONTENT,
            LAYOUT_FLAG,
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE,
            PixelFormat.TRANSLUCENT
        )
        windowManager!!.addView(customNotificationDialogView, params)
    }

    private fun initView(customNotificationDialogView:View) {
        ivAlertDialog = customNotificationDialogView.findViewById(R.id.ivAlertDialog)
        tvDesc = customNotificationDialogView.findViewById(R.id.tvAlertDialogdesc)
        btnOk = customNotificationDialogView.findViewById(R.id.btnAlertDialogOk)
        btnOk.setOnClickListener { close() }

        tvDesc.text = description
        ivAlertDialog.setImageResource(getIcon(icon))

    }

    private fun close() {
        try {
            (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).removeView(
                customNotificationDialogView
            )
            customNotificationDialogView!!.invalidate()
            (customNotificationDialogView!!.parent as ViewGroup).removeAllViews()
        } catch (e: Exception) {
            Log.d("Exception", e.toString())
        }
    }



}