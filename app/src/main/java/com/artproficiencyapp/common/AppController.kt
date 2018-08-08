package com.artproficiencyapp.common

import android.app.Application
import com.artproficiencyapp.receiver.ConnectivityReceiver

import com.facebook.drawee.backends.pipeline.Fresco

/**
 * Created by admin on 21-May-18.
 */

class AppController : Application() {

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
        instance = this
    }

    override fun onTerminate() {
        super.onTerminate()
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
    }

    fun setConnectivityListener(listener: ConnectivityReceiver.ConnectivityReceiverListener) {
        ConnectivityReceiver.connectivityReceiverListener = listener
    }

    companion object {
        @get:Synchronized
        var instance: AppController? = null
    }
}
