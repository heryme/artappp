package com.artproficiencyapp.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.artproficiencyapp.R
import com.artproficiencyapp.extension.goActivity

import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.parent_toolbar.*

class DirectorDashboardActiv : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        img_toolbar_edit!!.setImageResource(R.drawable.ic_edit)
        img_toolbar_back.visibility = View.GONE


        if (sessionManager.get("isFristProfileActivityLaunch", true)) {
            img_toolbar_edit!!.visibility = View.VISIBLE
            goActivity<DirectorProfileActivity>()
            finish()
            sessionManager.put("isFristProfileActivityLaunch", false)
        } else {
            img_toolbar_edit!!.visibility = View.GONE
            btnLogout.visibility = View.VISIBLE

        }

        btnLogout.setOnClickListener {
            val intent = Intent(this@DirectorDashboardActiv, LoginActivity::class.java)
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
            sessionManager.put("isFristProfileActivityLaunch", false)
          //  sessionManager.isLogin(false)

        }
        tv_toolbar_title.setText("Director Profile")
        tvDirUserDashBordActivityName.setText(getUserModel()!!.data.name)
        tvDirUserDashBordActivityDevelopment.setText("Under development")


        val email = sessionManager[getString(R.string.user_email), ""]
        val password = sessionManager[getString(R.string.user_password), ""]

        Log.e(TAG, "Edit Email value==  " + email)
        Log.e(TAG, "  Edit password value==  " + password)

        img_toolbar_edit!!.setOnClickListener {
            val intent = Intent(this@DirectorDashboardActiv, DirectorProfileActivity::class.java)
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()

        }
    }
}
