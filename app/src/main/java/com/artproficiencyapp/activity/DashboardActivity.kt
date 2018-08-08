package com.artproficiencyapp.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.artproficiencyapp.R
import com.artproficiencyapp.extension.goActivity
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.parent_toolbar.*

/**
 * Created by admin on 24-May-18.
 */
class DashboardActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        tv_toolbar_title.setText("Student Profile")
        developemrnt.setText("Under development")

        val email = sessionManager[getString(R.string.user_email), ""]
        val password = sessionManager[getString(R.string.user_password), ""]

        Log.e(TAG, "Edit Email value==  " + email)
        Log.e(TAG, "  Edit password value==  " + password)
        if (TextUtils.isEmpty(getUserModel()!!.data.name)) {
            tvUserName.text = "Hello User"
        } else {
            tvUserName.text = getUserModel()!!.data.name
        }

        btnLogoutStudent.setOnClickListener {
            val intent = Intent(this@DashboardActivity, LoginActivity::class.java)
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finishAffinity()
            sessionManager.isLogin(false)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.dashboard_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.menu_logout) {
            val email = sessionManager[getString(R.string.user_email), ""]
            val password = sessionManager[getString(R.string.user_password), ""]

            sessionManager.logout()
            sessionManager.put(getString(R.string.user_email), email)
            sessionManager.put(getString(R.string.user_password), password)
            goActivity<LoginActivity>()
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}