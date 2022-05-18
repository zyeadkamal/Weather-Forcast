package com.iti.mad42.weatherforcast.Utilities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class NetworkChangeReceiver(context: Context?) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        val status: Int = NetworkUtility.getConnectivityStatusString(context!!)
        if ("android.net.conn.CONNECTIVITY_CHANGE" == intent.action) {
            isAppOnline = status != NetworkUtility.NETWORK_STATUS_NOT_CONNECTED
        }
    }

    companion object {
        var isAppOnline = false
    }
}