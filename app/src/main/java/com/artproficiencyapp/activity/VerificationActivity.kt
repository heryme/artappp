package com.artproficiencyapp.activity

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.StyleSpan
import android.view.View
import com.artproficiencyapp.R
import android.os.CountDownTimer
import android.os.Handler
import com.artproficiencyapp.extension.*
import com.artproficiencyapp.restapi.ApiInitialize
import com.artproficiencyapp.restapi.ApiRequest
import com.artproficiencyapp.restapi.ApiResponseInterface
import com.artproficiencyapp.restapi.ApiResponseManager
import kotlinx.android.synthetic.main.activity_verification.*
import okhttp3.ResponseBody
import org.json.JSONObject
import android.widget.TextView

import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.support.v4.content.LocalBroadcastManager
import android.content.IntentFilter


class VerificationActivity : BaseActivity(), View.OnClickListener, ApiResponseInterface {

    private var directorCode = ""
    private var userID = ""
    private val TIMER = 300000L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)


        Initview()
        verifyNumber.text = getUserModel()!!.data.phone_number
        val spannableString = SpannableString(getString(R.string.dont_receive_code))
        spannableString.setSpan(StyleSpan(Typeface.BOLD), spannableString.length - 7, spannableString.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        txtResendCode.text = spannableString

        Handler().postDelayed({
            StartTimer()
        }, 100)


    }

    private fun StartTimer() {
        object : CountDownTimer(TIMER, 1000) {
            override fun onTick(millisUntilFinished: Long) {
//                MINUTE = millisUntilFinished / 60000
//                SECOND = (millisUntilFinished % 60000 / 1000)
                tvTimer.text = String.format("%02d:%02d", millisUntilFinished / 60000, (millisUntilFinished % 60000 / 1000))

                txtResendCode.visibility = View.GONE
                btnVerify.isEnabled = true
            }

            override fun onFinish() {
                tvTimer.text = "00:00"
                edtVerificationOne.setText("")
                edtVerificationTwo.setText("")
                edtVerificationThree.setText("")
                edtVerificationFour.setText("")
                btnVerify.isEnabled = false
                txtResendCode.visibility = View.VISIBLE
            }
        }.start()
    }

    private fun Initview() {
        btnVerify.setOnClickListener(this)
        //directorCode = intent.getStringExtra("director")
        //directorCode = getUserModel()!!.data.director_code

        //  LoggE(TAG, "Director code is ---- " + directorCode) //todo This code is set for temporary base because twillio is not setup properly, this will be removed

//        userID = intent.getStringExtra("id") //todo this value will be received from two class, one from student register screen and second from login
        LoggE(TAG, "User id -- " + getUserModel()!!.data.id.toString())

//        edtVerificationOne.setText(directorCode.toCharArray()[0].toString())
//        edtVerificationTwo.setText(directorCode.toCharArray()[1].toString())
//        edtVerificationThree.setText(directorCode.toCharArray()[2].toString())
//        edtVerificationFour.setText(directorCode.toCharArray()[3].toString())
        txtResendCode.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0) {
            btnVerify -> {
                if (IsValidInput()) {
                    hideSoftKeyboard(this)
                    if (isNetWork(this)) {
                        ApiRequest(this@VerificationActivity, ApiInitialize.initialize(ApiInitialize.MAIN_URl_API)
                                .verifyOTP("Bearer " + getUserModel()!!.data.token, getUserModel()!!.data.id.toString(),
                                        edtVerificationOne.value + edtVerificationTwo.value + edtVerificationThree.value + edtVerificationFour.value), OTP_VERIFY, true, this@VerificationActivity)
                    }
                }
            }
            txtResendCode -> {

                if (isNetWork(this)) {
                    ApiRequest(this@VerificationActivity, ApiInitialize.initialize(ApiInitialize.MAIN_URl_API)
                            .resendOTP("Bearer " + getUserModel()!!.data.token, getUserModel()!!.data.id.toString()), RESEND_OTP_VERIFY, true, this@VerificationActivity)
                }
            }
        }
    }

    private fun IsValidInput(): Boolean {
        if (TextUtils.isEmpty(edtVerificationOne!!.text.toString())) {
            showSnackBar(edtVerificationOne, getString(R.string.please_enter_verification))
            return false
        } else if (TextUtils.isEmpty(edtVerificationTwo!!.text.toString())) {
            showSnackBar(edtVerificationTwo, getString(R.string.please_enter_verification))
            return false
        } else if (TextUtils.isEmpty(edtVerificationThree!!.text.toString())) {
            showSnackBar(edtVerificationThree, getString(R.string.please_enter_verification))
            return false
        } else if (TextUtils.isEmpty(edtVerificationFour!!.text.toString())) {
            showSnackBar(edtVerificationFour, getString(R.string.please_enter_verification))
            return false
        }

        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {
            OTP_VERIFY -> {
                val responseBody = apiResponseManager.response as ResponseBody
                var responseString = responseBody.string()
                var jsonResponse = JSONObject(responseString)

                if (jsonResponse.optInt("status_code") == STATUS_CODE) {
                    goActivity<DashBordActivityDIreStu>()
                    finish()
                } else {
                    showMsgDialog(jsonResponse.optString("message"))
                }
            }
            RESEND_OTP_VERIFY -> {
                val responseBody = apiResponseManager.response as ResponseBody
                var responseString = responseBody.string()
                var jsonResponse = JSONObject(responseString)

                if (jsonResponse.optInt("status_code") == STATUS_CODE) {
                    btnVerify.isEnabled = true
                    Handler().postDelayed({
                        StartTimer()
                    }, 100)

                } else {
                    showMsgDialog(jsonResponse.optString("message"))
                }


            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action!!.equals("otp", ignoreCase = true)) {
                val message = intent.getStringExtra("message")
                directorCode = message
                edtVerificationOne.setText(directorCode.toCharArray()[0].toString())
                edtVerificationTwo.setText(directorCode.toCharArray()[1].toString())
                edtVerificationThree.setText(directorCode.toCharArray()[2].toString())
                edtVerificationFour.setText(directorCode.toCharArray()[3].toString())
            }

        }
    }

    public override fun onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, IntentFilter("otp"))
        super.onResume()
    }

    public override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }
}