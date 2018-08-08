package com.artproficiencyapp.fragment.commanfragment


import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.annotation.StyleRes
import android.support.v4.app.ActivityCompat.finishAffinity
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton

import com.artproficiencyapp.R
import com.artproficiencyapp.activity.EditProfileActivity
import com.artproficiencyapp.activity.LoginActivity
import com.artproficiencyapp.activity.TestInstructionActivity
import com.artproficiencyapp.common.FragmentHandler
import com.artproficiencyapp.extension.DIRECTOR
import com.artproficiencyapp.extension.Toast
import com.artproficiencyapp.extension.goActivity
import com.artproficiencyapp.fragment.student.StudentEditProfileFragment
import com.artproficiencyapp.test.fragment.TestListFragment
import kotlinx.android.synthetic.main.activity_embryology.*
import kotlinx.android.synthetic.main.app_bar_dash_bord_activity_dire_stu.*
import kotlinx.android.synthetic.main.fragment_setting.*


class SettingFragment : BaseFragment(), View.OnClickListener {
    private var objFragmentHandler = FragmentHandler()
    private val NIGHT_MODE = "night_mode"

    private var mSharedPref: SharedPreferences? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        setTitle("Settings")
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Initview()

    }

    private fun Initview() {
        txtEditProfile.setOnClickListener(this)
        txtReplayTutorial.setOnClickListener(this)
        txtInviteFriends.setOnClickListener(this)
        txtChangePassword.setOnClickListener(this)
        txtHelpAndFeedBack.setOnClickListener(this)
        txtTearmAndCondition.setOnClickListener(this)
        txtAboutsUs.setOnClickListener(this)
        txtLogout.setOnClickListener(this)
        mSharedPref = activity.getPreferences(Context.MODE_PRIVATE);


    }

    override fun onClick(p0: View?) {
        when (p0) {
            txtEditProfile -> {
                if (getUserModel()!!.data.user_type == DIRECTOR) {
                    val intent = Intent(activity, EditProfileActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    //activity.goActivity<EditProfileActivity>()
                   // activity.finish()
                } else {
                    replaceFragment(StudentEditProfileFragment(), "Edit Profile", null)
                }
            }
            txtReplayTutorial -> {
                showMsgDialog("Under development......")
            }
            txtInviteFriends -> {
                showMsgDialog("Under development......")
            }
            txtChangePassword -> {
                replaceFragment(ChangePasswordFragment(), "Change Password", null)
            }
            txtHelpAndFeedBack -> {
                showMsgDialog("Under development......")
            }
            txtTearmAndCondition -> {
                showMsgDialog("Under development......")
            }
            txtAboutsUs -> {
                showMsgDialog("Under development......")
            }
            txtLogout -> {
                openAlertDialog()
            }
        }
    }

    private fun replaceFragment(fragmentFlag: android.app.Fragment, tabName: String, bundle: Bundle?) {

        objFragmentHandler.replaceFragment(activity, R.id.simpleFrameLayout, fragmentFlag, null, bundle, true
                , tabName, 0, FragmentHandler.ANIMATION_TYPE.NONE)
    }

    private fun openAlertDialog() {
        val dialog = AlertDialog.Builder(activity, R.style.AppCompatAlertDialogStyle)
        dialog.setMessage(getString(R.string.are_you_log_out))
        dialog.setPositiveButton(R.string.dialog_yes) { _, _ ->
            sessionManager.isLogin(false)
            Toast("Logout", true, activity)
            val intent = Intent(activity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            activity.finish()


        }
        dialog.setNegativeButton(R.string.dialog_no) { dialog, _ ->
            dialog.cancel()
        }
        dialog.show()
    }



}

