package com.artproficiencyapp.activity

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.artproficiencyapp.R
import com.artproficiencyapp.extension.goActivity
import android.content.pm.PackageManager
import android.util.Base64
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class SplashActivity : BaseActivity() {

    private val SPLASH_TIME_OUT: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        printHashKey(this@SplashActivity)
//        if (getUserModel() != null)
//            Log.e(TAG, getUserModel()?.data?.name)


    }

    fun login() {
        Handler().postDelayed({
            when (sessionManager.isLogin) {
                true -> if (getUserModel()!!.data.user_type.equals("DIRECTOR", true)) {
                    goActivity<DashBordActivityDIreStu>()
                } else {

                    goActivity<DashBordActivityDIreStu>()
                }
                false -> goActivity<WelcomeActivity>()
            }
            finish()
        }, SPLASH_TIME_OUT)
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        super.onNetworkConnectionChanged(isConnected)
        if (isConnected) {
            login()
        } else {
            Log.e(TAG, "Not Connected")
        }
    }

    fun printHashKey(pContext: Context) {
        try {
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey = String(Base64.encode(md.digest(), 0))
                Log.e(TAG, "printHashKey() Hash Key: " + hashKey)
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e(TAG, "printHashKey()", e)
        } catch (e: Exception) {
            Log.e(TAG, "printHashKey()", e)
        }

    }

}