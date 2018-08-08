package com.artproficiencyapp.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.support.annotation.RequiresApi
import com.artproficiencyapp.common.AppController
import com.artproficiencyapp.extension.LoggE

/**
 * Created by admin on 12-Jul-18.
 */
class MyConnectionReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    companion object {
        @JvmStatic
        var connectivityReceiverListener: ConnectivityReceiverListener? = null

        fun isConnected(): Boolean {
            val cm = AppController.instance!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            var isConnected = false

            if (activeNetwork != null && activeNetwork.isConnectedOrConnecting) {
                isConnected = true
            }

            if (activeNetwork != null && activeNetwork.type == ConnectivityManager.TYPE_MOBILE && activeNetwork.isConnected) {
                isConnected = true
            }

            if (activeNetwork != null && activeNetwork.type == ConnectivityManager.TYPE_WIFI && activeNetwork.isConnected) {
                isConnected = true
            }
            return isConnected
        }
    }

    override fun onReceive(context: Context, arg1: Intent) {

        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo

        var isConnected = false

        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting) {
            isConnected = true
        }

        if (activeNetwork != null && activeNetwork.type == ConnectivityManager.TYPE_MOBILE && activeNetwork.isConnected) {
            isConnected = true
        }

        if (activeNetwork != null && activeNetwork.type == ConnectivityManager.TYPE_WIFI && activeNetwork.isConnected) {
            isConnected = true
        }

        LoggE(javaClass.simpleName, "OnReceive ------- $isConnected")

        if (connectivityReceiverListener != null) {
            connectivityReceiverListener!!.onNetworkConnectionChanged(isConnected)
        }
    }

    interface ConnectivityReceiverListener {
        fun onNetworkConnectionChanged(isConnected: Boolean)
    }
}