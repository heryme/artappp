package com.artproficiencyapp.fragment.commanfragment

import android.app.AlertDialog
import android.app.Dialog
import android.app.Fragment
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.TextView
import android.widget.Toast

import com.artproficiencyapp.R
import com.artproficiencyapp.activity.DashBordActivityDIreStu
import com.artproficiencyapp.common.SessionManager
import com.artproficiencyapp.model.Login
import com.artproficiencyapp.model.RegDirModel
import com.google.gson.Gson


/**
 * Created by admin on 18-May-18.
 */

open class BaseFragment : Fragment() {
    private var dialogg: Dialog? = null
    //  private var receiver: NetworkChangeReceiver? = null
    private var isConnected = false
    private var alertDialogBuilder: AlertDialog.Builder? = null


    internal lateinit var sessionManager: SessionManager

    private val userModel: RegDirModel
        get() {
            val gson = Gson()
            return gson.fromJson(sessionManager[getString(R.string.user_reg_data), ""], RegDirModel::class.java)
        }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(activity)
//        activity.registerReceiver(MyConnectivityReceiver(),
//                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

//        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
//        receiver = NetworkChangeReceiver()
//        activity.registerReceiver(receiver, filter)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sessionManager = SessionManager(activity)

    }



    protected fun showSnackBar(view: View, message: String) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        snackbar.view.setBackgroundColor(Color.BLACK)

        val tv = snackbar.view.findViewById<View>(android.support.design.R.id.snackbar_text) as TextView
        tv.setTextColor(Color.WHITE)

        snackbar.show()
    }

    fun getUserModel(): Login? {
        val gson = Gson()
        return gson.fromJson(sessionManager[getString(R.string.user_data), ""], Login::class.java)
    }

    protected fun showMsgDialog(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
    }

    fun setTitle(toolbarName: String) {
        (activity as DashBordActivityDIreStu).setToolbarTitle(toolbarName)
    }
    fun setToolbar(f:Boolean) {
        (activity as DashBordActivityDIreStu).setToolbarTransparent(f)
    }

//    fun setBackground(toolbarBackgroundColor: Boolean) {
//        (activity as DashBordActivityDIreStu).setToolbarBackgroundTransparent(toolbarBackgroundColor)
//    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode === 0) {
//            val intent = getIntent(data.toString())
//            startActivity(intent)
//        }
//
//
//    }


//    inner class NetworkChangeReceiver : BroadcastReceiver() {
//
//        override fun onReceive(context: Context, intent: Intent) {
//
//            Log.v("", "Receieved notification about network status")
//            isNetworkAvailable(context)
//
//        }
//
//
//        private fun isNetworkAvailable(context: Context): Boolean {
//            val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//            if (connectivity != null) {
//                val info = connectivity.allNetworkInfo
//                if (info != null) {
//                    for (i in info.indices) {
//                        if (info[i].state == NetworkInfo.State.CONNECTED) {
//                            if (!isConnected) {
//                                Log.v("Base Fragment", "Now you are connected to Internet!")
//                                isConnected = true
//                                if (isNetWork(activity)) {
//                                    alertDialogBuilder!!.create().dismiss()
//                                }
//
//                                //do your processing here ---
//                                //if you need to post any data to the server or get status
//                                //update from the server
//
//                            }
//                            return true
//                        }
//                    }
//                }
//            }
//
//            Log.v("", "You are not connected to Internet!")
//            alertDialogBuilder = AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
//            alertDialogBuilder!!
//                    .setMessage("No internet connection on your device. Would you like to enable it?")
//                    .setTitle("No Internet Connection")
//                    .setCancelable(false)
//                    .setPositiveButton(" Enable Internet ",
//                            DialogInterface.OnClickListener { dialog, id ->
//
//
//                                val dialogIntent = Intent(android.provider.Settings.ACTION_SETTINGS)
//                                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                                context.startActivity(dialogIntent)
//
//
//                                ///startActivityForResult(Intent(Settings.ACTION_SETTINGS), 0);
//                            })
//
//
//            val alert = alertDialogBuilder!!.create()
//            alert.show()
//            isConnected = false
//            return false
//        }
//    }


}