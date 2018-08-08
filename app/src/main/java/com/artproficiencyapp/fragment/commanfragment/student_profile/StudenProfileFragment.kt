package com.artproficiencyapp.fragment.commanfragment.student_profile

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.SearchView
import android.view.*
import com.artproficiencyapp.R
import com.artproficiencyapp.adapter.StudentProfileTestPagerAdapter
import com.artproficiencyapp.fragment.commanfragment.BaseFragment
import android.view.LayoutInflater
import com.artproficiencyapp.activity.EditProfileActivity
import com.artproficiencyapp.common.FragmentHandler
import com.artproficiencyapp.extension.DIRECTOR
import com.artproficiencyapp.extension.hideSoftKeyboard
import com.artproficiencyapp.extension.setMargins
import com.artproficiencyapp.fragment.student.StudentEditProfileFragment
import kotlinx.android.synthetic.main.fragment_student_profile.*
import kotlinx.android.synthetic.main.tab_student_profile_test.*


/**
 * A simple [Fragment] subclass.
 */
class StudenProfileFragment : BaseFragment(), ViewPager.OnPageChangeListener, View.OnClickListener {
    private var objFragmentHandler = FragmentHandler()
    private var tabSelectionIndex: Int = 0;
    var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity.window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setHasOptionsMenu(true);
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //setTitle("Student Profile")
        setToolbar(true)
        val contextThemeWrapper = ContextThemeWrapper(activity, R.style.StudentProfileTheme)
        val localInflater = inflater.cloneInContext(contextThemeWrapper)
        return localInflater.inflate(R.layout.fragment_student_profile, container, false)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView();
        setupViewPager(vPagerStudentProfile, TestCompletedFragment.newInstance(),
                TestNotAttempFragment.newInstance(),
                TestFailureFragment.newInstance())
        vPagerStudentProfile.addOnPageChangeListener(this)
        UpdateUI(0)
        hideSoftKeyboard(activity)
        if (getUserModel()!!.data.user_type == DIRECTOR) {
            setTitle("Director Profile")
        } else {
            setTitle("Student Profile")
        }
        //setBackground(true)
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        UpdateUI(position)
    }

    override fun onPageSelected(position: Int) {

    }

    override fun onClick(p0: View?) {
        when (p0) {
            tvTabStudentProfileTestCompleted -> {
                vPagerStudentProfile.setCurrentItem(0)
                UpdateUI(0)
                tabSelectionIndex = 0
                searchView?.setQuery("", false)
                //TestCompletedFragment.testAdapter?.notifyDataSetChanged()
                TestCompletedFragment.testAdapter?.getFilter()?.filter("")
            }

            tvTabStudentProfileTestNotAttempt -> {
                vPagerStudentProfile.setCurrentItem(1)
                UpdateUI(1)
                tabSelectionIndex = 1
                searchView?.setQuery("", false)
                TestNotAttempFragment.testAdapter?.getFilter()?.filter("")
                //TestNotAttempFragment.testAdapter?.notifyDataSetChanged()
            }

            tvTabStudentProfileTestFailure -> {
                vPagerStudentProfile.setCurrentItem(2)
                UpdateUI(2)
                tabSelectionIndex = 2
                searchView?.setQuery("", false)
                TestFailureFragment.testAdapter?.getFilter()?.filter("")
                //TestCompletedFragment.testAdapter?.notifyDataSetChanged()
            }
        }
    }

    private fun UpdateUI(pos: Int) {
        when (pos) {
            0 -> {
                tvTabStudentProfileTestCompleted.setBackgroundResource(R.drawable.ic_tab_selected)
                tvTabStudentProfileTestCompleted.setTextColor(resources.getColor(R.color.White))
                tvTabStudentProfileTestCompleted.setText(getString(R.string.test_completed))

                tvTabStudentProfileTestNotAttempt.setBackgroundResource(R.drawable.ic_tab_not_selected)
                tvTabStudentProfileTestNotAttempt.setTextColor(resources.getColor(R.color.white_alpha_50))
                tvTabStudentProfileTestNotAttempt.setText(getString(R.string.test_not_attemp))

                tvTabStudentProfileTestFailure.setBackgroundResource(R.drawable.ic_tab_not_selected)
                tvTabStudentProfileTestFailure.setTextColor(resources.getColor(R.color.white_alpha_50))
                tvTabStudentProfileTestFailure.setText(getString(R.string.test_failure))
            }
            1 -> {

                tvTabStudentProfileTestNotAttempt.setBackgroundResource(R.drawable.ic_tab_selected)
                tvTabStudentProfileTestNotAttempt.setTextColor(resources.getColor(R.color.White))
                tvTabStudentProfileTestNotAttempt.setText(getString(R.string.test_not_attemp))

                tvTabStudentProfileTestCompleted.setBackgroundResource(R.drawable.ic_tab_not_selected)
                tvTabStudentProfileTestCompleted.setTextColor(resources.getColor(R.color.white_alpha_50))
                tvTabStudentProfileTestCompleted.setText(getString(R.string.test_completed))

                tvTabStudentProfileTestFailure.setBackgroundResource(R.drawable.ic_tab_not_selected)
                tvTabStudentProfileTestFailure.setTextColor(resources.getColor(R.color.white_alpha_50))
                tvTabStudentProfileTestFailure.setText(getString(R.string.test_failure))
            }
            2 -> {

                tvTabStudentProfileTestFailure.setBackgroundResource(R.drawable.ic_tab_selected)
                tvTabStudentProfileTestFailure.setTextColor(resources.getColor(R.color.White))
                tvTabStudentProfileTestFailure.setText(getString(R.string.test_failure))

                tvTabStudentProfileTestNotAttempt.setBackgroundResource(R.drawable.ic_tab_not_selected)
                tvTabStudentProfileTestNotAttempt.setTextColor(resources.getColor(R.color.white_alpha_50))
                tvTabStudentProfileTestNotAttempt.setText(getString(R.string.test_not_attemp))

                tvTabStudentProfileTestCompleted.setBackgroundResource(R.drawable.ic_tab_not_selected)
                tvTabStudentProfileTestCompleted.setTextColor(resources.getColor(R.color.white_alpha_50))
                tvTabStudentProfileTestCompleted.setText(getString(R.string.test_completed))

            }
        }
    }

    /***
     * Set Up View Pager
     */
    private fun setupViewPager(viewPager: ViewPager, testCompletedFragment: TestCompletedFragment, testNotAttempFragment: TestNotAttempFragment,
                               testFailureFragment: TestFailureFragment) {
        val adapter = StudentProfileTestPagerAdapter(childFragmentManager)
        adapter.addFragment(testCompletedFragment, getString(R.string.test_completed))
        adapter.addFragment(testNotAttempFragment, getString(R.string.test_not_attemp))
        adapter.addFragment(testFailureFragment, getString(R.string.test_failure))
        viewPager.offscreenPageLimit = 3
        viewPager.adapter = adapter

        adapter.notifyDataSetChanged()
        viewPager.adapter!!.notifyDataSetChanged()
    }

    /**
     * Init view
     */
    fun initView() {
        tvTabStudentProfileTestCompleted.setOnClickListener(this)
        tvTabStudentProfileTestNotAttempt.setOnClickListener(this)
        tvTabStudentProfileTestFailure.setOnClickListener(this)
    }

    override
    fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.student_profile_fragment, menu);
        val search = menu.findItem(R.id.search)
        val edit = menu.findItem(R.id.edit)
        searchView = MenuItemCompat.getActionView(search) as SearchView
        searchView?.setBackgroundColor(Color.TRANSPARENT)
        search(searchView!!)

        //SearchView Back Arrow Click Event Handle
        search.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                llStudentFragmentTab.visibility = View.GONE
                llStudentFragmentProfile.visibility = View.GONE
                setMargins(llViewpager, 0, 150, 0, 0)
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                llStudentFragmentTab.visibility = View.VISIBLE
                llStudentFragmentProfile.visibility = View.VISIBLE
                //Search Time Margin Set Viewpager
                setMargins(llViewpager, 0, 0, 0, 0)
                return true
            }
        })


    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.getItemId()) {
            R.id.edit -> {
                if (getUserModel()!!.data.user_type == DIRECTOR) {
                    searchView!!.setFocusable(false);
                    val intent = Intent(activity, EditProfileActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                } else {
                    replaceFragment(StudentEditProfileFragment(), "Edit Profile", null)
                }

                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun replaceFragment(fragmentFlag: android.app.Fragment, tabName: String, bundle: Bundle?) {

        objFragmentHandler.replaceFragment(activity, R.id.simpleFrameLayout, fragmentFlag, null, bundle, true
                , tabName, 0, FragmentHandler.ANIMATION_TYPE.NONE)
    }


    /***
     * Search From Here For Test Completed,Test Not Attempt,Test Failure
     */
    private fun search(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (tabSelectionIndex == 0) {
                    TestCompletedFragment.testAdapter?.getFilter()?.filter(newText)
                } else if (tabSelectionIndex == 1) {
                    TestNotAttempFragment.testAdapter?.filter?.filter(newText)
                } else if (tabSelectionIndex == 2) {
                    TestFailureFragment.testAdapter?.filter?.filter(newText)
                }
                return true
            }
        })
    }
}