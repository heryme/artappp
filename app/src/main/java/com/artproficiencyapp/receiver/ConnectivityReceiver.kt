package com.artproficiencyapp.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.support.annotation.RequiresApi
import com.artproficiencyapp.common.AppController
import com.artproficiencyapp.common.JobShe
import com.artproficiencyapp.extension.LoggE


/** Author : http://devdeeds.com
 *  Project : Sample Project - Internet status checking
 *  Date : 24 Feb 2018*/

class ConnectivityReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("UnsafeProtectedBroadcastReceiver")


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
       // JobShe.scheduleJob(context);
    }




    interface ConnectivityReceiverListener {
        fun onNetworkConnectionChanged(isConnected: Boolean)
    }

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
}
