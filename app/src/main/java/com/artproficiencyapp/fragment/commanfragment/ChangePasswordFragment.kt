package com.artproficiencyapp.fragment.commanfragment


import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

import com.artproficiencyapp.R
import com.artproficiencyapp.extension.Toast
import com.artproficiencyapp.utils.Validator

import kotlinx.android.synthetic.main.fragment_change_password.*


class ChangePasswordFragment : BaseFragment(), View.OnClickListener {
    private var isPasswordVisible = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        setTitle("Change Password")
        return inflater.inflate(R.layout.fragment_change_password, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Initview()
        showPassword()
    }

    private fun Initview() {
        btnChangepassword.setOnClickListener(this)
    }

    private fun showPassword() {
        edtOldPassword!!.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= edtOldPassword!!.right - edtOldPassword!!.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                    // your action here
                    if (isPasswordVisible)

                        OldpasswordVisable(true)
                    else
                        OldpasswordVisable(false)
                    return@OnTouchListener true
                }
            }
            false
        })

        edtNewPassword!!.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= edtNewPassword!!.right - edtNewPassword!!.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                    // your action here
                    if (isPasswordVisible)

                        NewpasswordVisable(true)
                    else
                        NewpasswordVisable(false)
                    return@OnTouchListener true
                }
            }
            false
        })

        edtConformPassword!!.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= edtConformPassword!!.right - edtConformPassword!!.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                    // your action here
                    if (isPasswordVisible)

                        ConformpasswordVisable(true)
                    else
                        ConformpasswordVisable(false)
                    return@OnTouchListener true
                }
            }
            false
        })


    }

    private fun OldpasswordVisable(_mVisible: Boolean) {
        if (_mVisible) {
            isPasswordVisible = false
            edtOldPassword!!.transformationMethod = HideReturnsTransformationMethod.getInstance()
        } else {
            isPasswordVisible = true

            edtOldPassword!!.transformationMethod = PasswordTransformationMethod.getInstance()
        }
    }

    private fun NewpasswordVisable(_mVisible: Boolean) {
        if (_mVisible) {
            isPasswordVisible = false
            edtNewPassword!!.transformationMethod = HideReturnsTransformationMethod.getInstance()
        } else {
            isPasswordVisible = true

            edtNewPassword!!.transformationMethod = PasswordTransformationMethod.getInstance()
        }
    }

    private fun ConformpasswordVisable(_mVisible: Boolean) {
        if (_mVisible) {
            isPasswordVisible = false
            edtConformPassword!!.transformationMethod = HideReturnsTransformationMethod.getInstance()
        } else {
            isPasswordVisible = true

            edtConformPassword!!.transformationMethod = PasswordTransformationMethod.getInstance()
        }
    }

    override fun onClick(p0: View?) {
        when (p0) {
            btnChangepassword -> {
                if (IsValidInput()) {
                    Toast("Under Development", true, activity)
                }

            }
        }
    }

    private fun IsValidInput(): Boolean {
        if (TextUtils.isEmpty(edtOldPassword!!.text.toString())) {
            showSnackBar(edtOldPassword, getString(R.string.error_old_password))
            return false
        } else if (TextUtils.isEmpty(edtNewPassword!!.text.toString())) {
            showSnackBar(edtNewPassword, getString(R.string.error_new_password))
            return false
        } else if (TextUtils.isEmpty(edtConformPassword!!.text.toString())) {
            showSnackBar(edtConformPassword, getString(R.string.error_conform_password))
            return false
        } else if (edtNewPassword!!.text.toString().trim { it <= ' ' }.length < 6) {
            showSnackBar(edtNewPassword, getString(R.string.error_pwd_msg))
            return false
        } else if (edtConformPassword!!.text.toString().trim { it <= ' ' }.length < 6) {
            showSnackBar(edtNewPassword, getString(R.string.error_pwd_msg))
            return false
        } else if (!Validator.isValidPassword(edtNewPassword.text.toString().trim())) {
            showSnackBar(edtNewPassword, getString(R.string.error_pwd_invalid_msg))
            return false
        } else if (!Validator.isValidPassword(edtConformPassword.text.toString().trim())) {
            showSnackBar(edtConformPassword, getString(R.string.error_pwd_invalid_msg))
            return false
        } else if (edtNewPassword!!.text.toString() != edtConformPassword!!.text.toString()) {
            showSnackBar(edtConformPassword, getString(R.string.error_pwd_diff))
            return false
        }
        return true
    }
}
