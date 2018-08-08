package com.artproficiencyapp.restapi

import android.accounts.NetworkErrorException
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.artproficiencyapp.R
import com.artproficiencyapp.activity.LoginActivity
import com.artproficiencyapp.common.SessionManager
import com.artproficiencyapp.extension.LoggE
import com.artproficiencyapp.extension.Toast
import com.artproficiencyapp.extension.dismissDialog
import com.artproficiencyapp.extension.getProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import java.text.ParseException
import java.util.concurrent.TimeoutException

@SuppressLint("ParcelCreator")
class ApiRequest<T>(private val activity: Activity, objectType: T, private val TYPE: Int, private val isShowProgressDialog: Boolean, private val apiResponseInterface: ApiResponseInterface) : Callback<T>, Parcelable {

    private var mProgressDialog: ProgressDialog? = null
    private var retryCount = 0
    private var call: Call<T>? = null

    init {
        showProgress()
        call = objectType as Call<T>
        call!!.enqueue(this)
    }

    private fun showProgress() {
        if (isShowProgressDialog) {
            mProgressDialog =getProgressDialog(activity)
        }
    }

    private fun dismissProgress() {
        if (isShowProgressDialog) {
            dismissDialog(activity, mProgressDialog!!)
        }
    }

    override fun onResponse(call: Call<T>, response: Response<T>) {
        dismissProgress()
       LoggE("Request URL123===> ", call.request().url().toString()+response.code())
        //                Logg.e("2.0 getFeed > Full json res wrapped in gson => ", new Gson().toJson(response));
        if (response.isSuccessful) {
            Log.e(TAG, "API response --- " + response.body())
            apiResponseInterface.getApiResponse(ApiResponseManager(response.body(), TYPE))
        } else {
            Toast(response.message(), false, activity)
            val error = ErrorUtils.parseError(response)
            Toast(error.message(), false, activity)
            LoggE("error.status()==>", error.status())
            LoggE("error.message()==>", error.message())
            LoggE("status_code==>", error.status_code!!)

            if (error.status() == 401) {
                if (SessionManager.instance[activity.resources.getString(R.string.remember_credential), false]) {
                    val email = SessionManager.instance[activity.resources.getString(R.string.email), ""]
                    val password = SessionManager.instance[activity.resources.getString(R.string.password), ""]
                    SessionManager.instance.logout()
                    SessionManager.instance.put(activity.resources.getString(R.string.email), email)
                    SessionManager.instance.put(activity.resources.getString(R.string.password), password)
                    SessionManager.instance.put(activity.resources.getString(R.string.remember_credential), true)
                } else {
                    SessionManager.instance.logout()
                }
                val intent = Intent(activity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                activity.startActivity(intent)
                activity.finishAffinity()
            }
        }
    }

    override fun onFailure(call: Call<T>, error: Throwable) {
        error.printStackTrace()
        LoggE("REQUEST", "ERROR")
        LoggE("REQUEST====", error.message!!)
        if (retryCount++ < TOTAL_RETRIES) {
            Log.v(TAG, "Retrying... ($retryCount out of ${TOTAL_RETRIES})")
            retry()
            return
        }
        dismissProgress()
        when (error) {
            is NetworkErrorException -> Toast(activity.resources.getString(R.string.toast_time_out), false, activity)
            is TimeoutException -> Toast(activity.resources.getString(R.string.toast_time_out), false, activity)
            is SocketTimeoutException -> Toast(activity.resources.getString(R.string.toast_try_after_sometimes), false, activity)
            is ParseException -> Toast(activity.resources.getString(R.string.toast_something_wrong), false, activity)
        }
    }
    private fun retry() {
        call!!.clone().enqueue(this)
    }

    companion object {
        private val TAG = "ApiRequest"
        private val TOTAL_RETRIES = 7
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(TYPE)
        parcel.writeByte(if (isShowProgressDialog) 1 else 0)
        parcel.writeInt(retryCount)
    }

    override fun describeContents(): Int {
        return 0
    }
}