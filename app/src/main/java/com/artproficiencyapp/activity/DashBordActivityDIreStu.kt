package com.artproficiencyapp.activity

import android.app.Fragment
import android.app.FragmentManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v7.app.ActionBarDrawerToggle
import com.artproficiencyapp.R

import com.artproficiencyapp.fragment.director.TestFragment
import com.artproficiencyapp.test.fragment.TestMenuFragment
import com.facebook.drawee.view.SimpleDraweeView
import com.artproficiencyapp.common.FragmentHandler
import kotlinx.android.synthetic.main.activity_dash_bord_dire_stu.*
import kotlinx.android.synthetic.main.app_bar_dash_bord_activity_dire_stu.*
import android.support.v4.widget.DrawerLayout
import android.widget.TextView
import com.artproficiencyapp.fragment.student.GlobalCompentencyMaplFragment
import android.graphics.Color
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import com.artproficiencyapp.chat.fragment.ChatFragment
import com.artproficiencyapp.common.RegularTextview
import com.artproficiencyapp.extension.*
import com.artproficiencyapp.fragment.commanfragment.SettingFragment
import com.artproficiencyapp.fragment.commanfragment.student_profile.StudenProfileFragment
import com.artproficiencyapp.fragment.commanfragment.InboxFragment
import com.artproficiencyapp.fragment.director.LabStatisticsFragment
import com.artproficiencyapp.fragment.commanfragment.NotificationFragment
import com.artproficiencyapp.fragment.commanfragment.PerformanceReportFragment
import com.artproficiencyapp.fragment.student.StudentDashboardFragment
import kotlinx.android.synthetic.main.content_dash_bord_activity_dire_stu.*

class DashBordActivityDIreStu : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var mViewHeaderLeft: View? = null
    var navViewLeft: NavigationView? = null
    private var toggle: ActionBarDrawerToggle? = null
    private var objFragmentHandler = FragmentHandler()
    private var tvChatUnreadCount: TextView? = null
    private var tabLayoutPosition: Int? = 0
    private var toolbarName: RegularTextview? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_bord_dire_stu)

        toolbarName = toolbar.findViewById(R.id.txttoolbarName) as RegularTextview
        toolbarName!!.setText("Menu 1");
        toolbar.setNavigationIcon(R.drawable.menu_icon)
        setSupportActionBar(toolbar)
        toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle!!)
        toggle!!.syncState()
        nav_view.setNavigationItemSelectedListener(this)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toggle?.setToolbarNavigationClickListener {
            //fragment.onBackPressed();
            onBackPressed()
        }

        //Show Hide Navigation Drawer Item According To Student OR Director
        showHideNavigationItem()
        //Change Navigation Item Font Style
        changeNavigationFontStyle(nav_view, this@DashBordActivityDIreStu)
        mViewHeaderLeft = nav_view!!.getHeaderView(0);
        val imageUri = Uri.parse("https://i.imgur.com/tGbaZCY.jpg")
        val mProfileView = mViewHeaderLeft!!.findViewById<SimpleDraweeView>(R.id.nvProfileImage);
        mProfileView.setImageURI(imageUri)
        val Username = mViewHeaderLeft!!.findViewById<RegularTextview>(R.id.DashBordUsername);
        val UserEmail = mViewHeaderLeft!!.findViewById<RegularTextview>(R.id.DashBordUserEmail);
        Username.text = getUserModel()!!.data.name
        UserEmail.text = getUserModel()!!.data.email
        onNavigationItemSelected(nav_view.getMenu().getItem(0));
        setMenuCounter("3")
        //setUserType()
        fragmentManager.addOnBackStackChangedListener(onBackStackChangedListener)

        //Open Student Profile Fragment Test
        val headerProfile = mViewHeaderLeft!!.findViewById<LinearLayout>(R.id.headerProfile);
        headerProfile.setOnClickListener(View.OnClickListener {

            if (getUserModel()!!.data.user_type == DIRECTOR) {
                goActivity<DirectorProfileActivity>()
                 finish()
            } else {
                val size = nav_view.getMenu().size()
                for (i in 0 until size) {
                    nav_view.getMenu().getItem(i).setChecked(false)
                }
                replaceFragmentt(StudenProfileFragment(), "Student Profile", null)
                drawer_layout.closeDrawer(GravityCompat.START)
                setToolbarTransparent(true)
            }

        })

        /* val menu = nav_view.getMenu()
         val nav_camara = menu.findItem(R.id.nav_labStatistics)
         if (getUserModel()!!.data.user_type == DIRECTOR) {
             nav_camara.setTitle("Lab Statistics")
         } else {
             nav_camara.setTitle("Performance Report")
         }*/

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var editProfFrag = fragmentManager.findFragmentByTag("Edit Profile")
        editProfFrag.onActivityResult(requestCode, resultCode, data)
        LoggE(javaClass.simpleName, "In onActivityResult Method")
        if (editProfFrag != null) {
            LoggE(javaClass.simpleName, "Not null")
        } else {
            LoggE(javaClass.simpleName, "Null")
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                if (getUserModel()!!.data.user_type == DIRECTOR) {
                    setToolbarTransparent(false)
                    replaceFragment(TestFragment(), getString(R.string.home_director), 0, null)

                } else {
                    setToolbarTransparent(false)
                    replaceFragment(StudentDashboardFragment(), getString(R.string.home_student), 0, null)

                }
            }
            R.id.nav_notification -> {
                setToolbarTransparent(false)
                replaceFragment(NotificationFragment(), getString(R.string.notification), 1, null)

            }
            R.id.nav_testMenu -> {
                replaceFragment(TestMenuFragment(), getString(R.string.test_menu_), 2, null)
                setToolbarTransparent(false)
            }
            R.id.nav_inbox -> {
                setToolbarTransparent(false)
                replaceFragment(InboxFragment(), getString(R.string.inbox), 3, null)
            }
            R.id.nav_labStatistics -> {
                setToolbarTransparent(false)
                replaceFragment(LabStatisticsFragment(), getString(R.string.lab_statistics), 4, null)
            }

            R.id.nav_performance_report -> {
                setToolbarTransparent(false)
                replaceFragment(PerformanceReportFragment(), getString(R.string.performance_report), 5, null)
            }
            R.id.nav_global_Map -> {
                setToolbarTransparent(false)
                replaceFragment(GlobalCompentencyMaplFragment(), getString(R.string.global_company_map), 6, null)
            }
            R.id.nav_setting -> {
                setToolbarTransparent(false)
                replaceFragment(SettingFragment(), getString(R.string.setting), 7, null)
            }
            R.id.nav_aboutsUs -> {
                setToolbarTransparent(false)

            }
//            R.id.nav_signOut -> {
//                setToolbarTransparent(false)
//                openAlertDialog()
//            }
        }

        drawer_layout.closeDrawer(Gravity.LEFT)
        return true
    }

//    private fun openAlertDialog() {
//        val dialog = AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
//        dialog.setMessage(getString(R.string.are_you_log_out))
//        dialog.setPositiveButton(R.string.dialog_yes) { _, _ ->
//
//            Toast("Logout", true, this@DashBordActivityDIreStu)
//            val intent = Intent(this@DashBordActivityDIreStu, LoginActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            startActivity(intent)
//            finishAffinity()
//            sessionManager.isLogin(false)
//        }
//        dialog.setNegativeButton(R.string.dialog_no) { dialog, _ ->
//            dialog.cancel()
//        }
//        dialog.show()
//    }

    private fun setMenuCounter(count: String) {
        tvChatUnreadCount = nav_view.getMenu().findItem(R.id.nav_inbox).getActionView() as TextView
        tvChatUnreadCount!!.text = count
    }

    private fun clearBackStack() {
        val fragmentManager = fragmentManager
        for (i in 0 until fragmentManager.backStackEntryCount) {
            // Get the back stack fragment id.
            val first = fragmentManager.getBackStackEntryAt(0)
            fragmentManager.popBackStack(first.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    fun setToolbarTitle(tabName: String) {
        toolbarName!!.text = tabName

    }

    private val onBackStackChangedListener = FragmentManager.OnBackStackChangedListener {
        if (fragmentManager.backStackEntryCount == 0) {
            toggle?.isDrawerIndicatorEnabled = true
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            toolbar.setNavigationIcon(R.drawable.menu_icon)

        } else {
            toggle?.isDrawerIndicatorEnabled = false
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolbar.setNavigationIcon(R.drawable.back_icon)
            drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        }
    }

    private fun replaceFragment(fragmentFlag: Fragment, tabName: String, position: Int, bundle: Bundle?) {
        clearBackStack()
        toggle?.isDrawerIndicatorEnabled = true
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        toolbar.setNavigationIcon(R.drawable.menu_icon)
        toolbarName!!.text = tabName
        LoggE("", "tab position==>$position")
        nav_view.menu.getItem(position).isChecked = true
        objFragmentHandler.replaceFragment(this, R.id.simpleFrameLayout, fragmentFlag, null, bundle, false, tabName, 0, FragmentHandler.ANIMATION_TYPE.NONE)
    }

    private fun replaceFragmentt(fragmentFlag: Fragment, tabName: String, bundle: Bundle?) {
        clearBackStack()
        toggle?.isDrawerIndicatorEnabled = true
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        toolbar.setNavigationIcon(R.drawable.menu_icon)
        toolbarName!!.text = tabName
        objFragmentHandler.replaceFragment(this, R.id.simpleFrameLayout, fragmentFlag, null, bundle, false, tabName, 0, FragmentHandler.ANIMATION_TYPE.NONE)
    }

    override fun onBackPressed() {
        Log.d("TAG", "backStackEntryCount---->" + fragmentManager.backStackEntryCount)
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        } else {
            if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                drawer_layout.closeDrawer(GravityCompat.START)
            } else {
                super.onBackPressed()
            }

        }
    }

    fun setToolbarTransparent(isTransparent: Boolean) {
        if (isTransparent) {
            coordinateLayoutDashBoardActivityDireStuMain.fitsSystemWindows = false
            toolbar.setBackgroundColor(Color.TRANSPARENT);
            //Set Margin Toolbar
            setMargins(toolbar, 0, 60, 0, 0)
            setMargins(simpleFrameLayout, 0, 0, 0, 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(Color.TRANSPARENT);
            }

        } else {

            //Set Margin For Frame Layout According To Action Bar Height
            setMargins(simpleFrameLayout, 0, getActionBarHeight(), 0, 0)
            setMargins(toolbar, 0, 0, 0, 0)
            coordinateLayoutDashBoardActivityDireStuMain.fitsSystemWindows = true
            toolbar.setBackgroundColor(ContextCompat.getColor(this@DashBordActivityDIreStu, R.color.colorPrimary))
            //window.statusBarColor = ContextCompat.getColor(this@DashBordActivityDIreStu, R.color.colorPrimary)

            //Change Status Bar Color
            val window = getWindow()
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.setStatusBarColor(ContextCompat.getColor(this@DashBordActivityDIreStu, R.color.colorPrimary))


        }


    }

    /**
     * Show Hide Navigation Drawer Item According To Student OR Director
     */
    private fun showHideNavigationItem() {
        val menu = nav_view.getMenu()
        val nav_labStatistics = menu.findItem(R.id.nav_labStatistics)
        val performance_report = menu.findItem(R.id.nav_performance_report)

        if (getUserModel()!!.data.user_type == DIRECTOR) {
            nav_labStatistics.setVisible(true)
            performance_report.setVisible(false)
        } else {
            nav_labStatistics.setVisible(false)
            performance_report.setVisible(true)
        }

    }

    /***
     * Get Actionbar Hight
     */
    fun getActionBarHeight(): Int {
        val ta = theme.obtainStyledAttributes(
                intArrayOf(android.R.attr.actionBarSize))
        return ta.getDimension(0, 0f).toInt()
    }
}
