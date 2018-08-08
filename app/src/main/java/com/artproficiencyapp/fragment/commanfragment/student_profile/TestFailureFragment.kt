package com.artproficiencyapp.fragment.commanfragment.student_profile


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.artproficiencyapp.R
import com.artproficiencyapp.adapter.TestAdapter

import com.artproficiencyapp.fragment.commanfragment.BaseFragment
import com.facebook.FacebookSdk
import kotlinx.android.synthetic.main.fragment_test_failure.*
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class TestFailureFragment : BaseFragment() {

    private var testList: MutableList<String>? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_test_failure, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView();
        setAdapter()

    }

    /**
     * Init View
     */
    private fun initView() {
        testList = ArrayList()
    }

    private fun setAdapter() {
        testList?.add("Sperm Morohology Slide Crading");
        testList?.add("Round Cell Diffrentials");
        testList?.add("Sperm Morohology Slide Crading");
        testList?.add("Round Cell Diffrentials");
        testList?.add("Sperm Morohology Slide Crading");
        testList?.add("Round Cell Diffrentials");



        testAdapter = TestAdapter(activity, testList as ArrayList<String>)
        val mLayoutManager = LinearLayoutManager(FacebookSdk.getApplicationContext())
        rvFragmentTestFailure.setLayoutManager(mLayoutManager)
        rvFragmentTestFailure.setItemAnimator(DefaultItemAnimator())
        rvFragmentTestFailure.setAdapter(testAdapter)

    }

    companion object {
        var testAdapter: TestAdapter? = null
        fun newInstance(): TestFailureFragment {
            val args = Bundle()
            val fragment = TestFailureFragment()
            fragment.arguments = args
            return fragment
        }
    }


}
