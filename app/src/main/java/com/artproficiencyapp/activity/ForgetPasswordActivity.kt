package com.artproficiencyapp.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.artproficiencyapp.R
import com.artproficiencyapp.extension.*
import com.artproficiencyapp.restapi.ApiInitialize
import com.artproficiencyapp.restapi.ApiRequest
import com.artproficiencyapp.restapi.ApiResponseInterface
import com.artproficiencyapp.restapi.ApiResponseManager
import com.artproficiencyapp.utils.Validator
import kotlinx.android.synthetic.main.activity_director.*
import kotlinx.android.synthetic.main.activity_forget_password.*
import kotlinx.android.synthetic.main.parent_toolbar.*
import okhttp3.ResponseBody
import org.json.JSONObject

class ForgetPasswordActivity : BaseActivity(), View.OnClickListener, ApiResponseInterface {

    override fun onClick(p0: View?) {
        when (p0) {
            btnForgotPasswordd -> {

                        if (IsValidInput()) {
                            hideSoftKeyboard(this)
                            if (isNetWork(this)) {
                                ApiRequest(this@ForgetPasswordActivity, ApiInitialize.initialize(ApiInitialize.MAIN_URl_API).forgotPassword(edtForgotPassword.value), FORGOTPASSWORD, true, this)
                            }
                        }
            }
            img_toolbar_back -> {
                val i = Intent(this, LoginActivity::class.java)
                startActivity(i)
                // close this activity
                finish()
            }


        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)
        val toolbar = findViewById<View>(R.id.parent_toolbar) as Toolbar // get the reference of Toolbar
        setSupportActionBar(toolbar)
        InitViews()
    }

    private fun InitViews() {
        img_toolbar_back.setOnClickListener(this)
        btnForgotPasswordd.setOnClickListener(this)
        tv_toolbar_title.text = getString(R.string.forgot_password)
    }
    private fun IsValidInput(): Boolean {
        if (TextUtils.isEmpty(edtForgotPassword!!.text.toString().trim { it <= ' ' })) {
            showSnackBar(edtForgotPassword, getString(R.string.toast_email))
            return false
        } else if (!Validator.validateEmail(edtForgotPassword!!.text.toString().trim { it <= ' ' })) {
            showSnackBar(edtForgotPassword, getString(R.string.error_valid_email))
            return false

        }
        return true
    }
    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {

            FORGOTPASSWORD -> {
                hideSoftKeyboard(this)
                val model = apiResponseManager.response as ResponseBody
                val responseValue = model.string()
                Log.e(TAG, responseValue)
                val response: JSONObject = JSONObject(responseValue)
                Toast(response.optString(MESSAGE), false, this)
                if (response.optInt("status_code") == STATUS_CODE) {
//                    LoggE(TAG, "Token==>${getUserModel()?.data!!.token}")
                    this.goActivity<LoginActivity>()
                    finish()
                }

            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        goActivity<LoginActivity>()
    }
}