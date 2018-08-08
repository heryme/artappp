package com.artproficiencyapp.fragment.commanfragment.student_profile


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artproficiencyapp.R
import com.artproficiencyapp.fragment.commanfragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_test_completed.*
import android.support.v7.widget.DefaultItemAnimator
import com.facebook.FacebookSdk.getApplicationContext
import android.support.v7.widget.LinearLayoutManager
import com.artproficiencyapp.adapter.TestAdapter


/**
 * A simple [Fragment] subclass.
 */
class TestCompletedFragment : BaseFragment() {


   // private var testAdapter: TestAdapter? = null
    private var testList: MutableList<String>? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_test_completed, container, false)
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
        val mLayoutManager = LinearLayoutManager(getApplicationContext())
        rvFragmentTestComp.setLayoutManager(mLayoutManager)
        rvFragmentTestComp.setItemAnimator(DefaultItemAnimator())
        rvFragmentTestComp.setAdapter(testAdapter)

    }

    companion object {
        var testAdapter: TestAdapter? = null
        fun newInstance(): TestCompletedFragment {
            val args = Bundle()
            val fragment = TestCompletedFragment()
            fragment.arguments = args
            return fragment
        }
    }



}
