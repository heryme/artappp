package com.artproficiencyapp.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar

import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import android.widget.Toast

import com.artproficiencyapp.R
import com.artproficiencyapp.common.SessionManager
import com.artproficiencyapp.model.Login
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.gson.Gson
import java.util.*
import android.net.ConnectivityManager
import android.os.CountDownTimer
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import com.artproficiencyapp.common.AppController
import com.artproficiencyapp.extension.*
import com.artproficiencyapp.receiver.ConnectivityReceiver


/**
 * Created by admin on 18-May-18.
 */
@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {
    private var dialogg: Dialog? = null
    lateinit var sessionManager: SessionManager
    val TAG: String = javaClass.simpleName
    private var callbackManager: CallbackManager? = null
    private var alertDialogBuilder: AlertDialog.Builder? = null
    private var mSnackBar: Snackbar? = null
    var isConnected: Boolean = false
    var connectivityReceiver: ConnectivityReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(this)
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        dialogg = Dialog(this, R.style.DialogCustomTheme)
        connectivityReceiver = ConnectivityReceiver()
        registerReceiver(ConnectivityReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        var countDownTimer: CountDownTimer
        checkConnection()
    }

    private fun checkConnection() {
        val isConnected: Boolean = ConnectivityReceiver.isConnected()
        this.isConnected = isConnected
        showMessage(isConnected)
    }

    private fun GoToTargetActivity(mContext: Context, mActivityClass: Class<*>) {
        val intent = Intent(applicationContext, mActivityClass)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right_to_left, R.anim.slide_out_right_to_left)
        finish()
    }

    protected fun showSnackBar(view: View, message: String) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        snackbar.view.setBackgroundColor(Color.BLACK)
        val tv = snackbar.view.findViewById<View>(android.support.design.R.id.snackbar_text) as TextView
        tv.setTextColor(Color.WHITE)
        snackbar.show()
    }

    override fun onResume() {
        super.onResume()
        AppController.instance!!.setConnectivityListener(this)
    }

    fun showMessage(isConnected: Boolean) {
        this.isConnected = isConnected
        if (!isConnected) {
            dialogg!!.setContentView(R.layout.item_newconnection_check_dialog)
            dialogg!!.setCancelable(false)
            dialogg!!.setCanceledOnTouchOutside(false)
            dialogg!!.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialogg!!.getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH)
            val edexit = dialogg!!.findViewById<Button>(R.id.edexit)
            dialogg!!.show()
            edexit.setOnClickListener {
                finishAffinity()
            }
        } else {
            dialogg!!.dismiss()

        }
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        LoggE(TAG, "isConnected --- {$isConnected}")
        showMessage(isConnected)
    }

    fun loginSocial(context: Context) {
        loginFaceBook()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }

    protected fun showMsgDialog(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    fun getUserModel(): Login? {
        val gson = Gson()
        return gson.fromJson(sessionManager[getString(R.string.user_data), ""], Login::class.java)
    }

    fun connectWithFaceBook() {
        if (AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut()
            LoginManager.getInstance().logInWithReadPermissions(this@BaseActivity, Arrays.asList("public_profile", "user_friends", "email"))
        } else {
            LoginManager.getInstance().logInWithReadPermissions(this@BaseActivity, Arrays.asList("public_profile", "user_friends", "email"))
        }
    }

    private fun loginFaceBook() {
        LoggE("Login with facebook=======>", "vkfnvknfknvbkfnkvbf")
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                LoggE("Success", ":-")
                val request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken()) { `object`, response ->
                    getFacebookData(response)
                }
                val parameters = Bundle()
                parameters.putString("fields", "id,email,first_name,last_name,gender,name,birthday")
                request.parameters = parameters
                request.executeAsync()
            }

            override fun onCancel() {
                // Toast("Login Cancel", false, this@BaseActivity)
                Toast("Login Cancel", false, this@BaseActivity)
            }

            override fun onError(error: FacebookException?) {
                if (error is FacebookAuthorizationException) {
                    if (AccessToken.getCurrentAccessToken() != null) {
                        LoginManager.getInstance().logOut()
                    }
                }
            }

        })
    }

    open fun getFacebookData(response: GraphResponse) {
        LoggE("Basactivity==", "getFacebookData")
    }
}