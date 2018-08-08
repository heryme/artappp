package com.artproficiencyapp.fragment.student

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artproficiencyapp.R
import com.artproficiencyapp.extension.Toast
import com.artproficiencyapp.fragment.commanfragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_student_dashboard.*


class StudentDashboardFragment : BaseFragment(), View.OnClickListener {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_dashboard, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        registerClickEvent();

    }

    override fun onClick(view: View?) {
        when (view) {
            cardViewStudentFragmentDashLabDirector -> {
                Toast("Under Development",true,activity)
            }

            cardViewStudentFragmentDashTestMenu -> {
                Toast("Under Development",true,activity)
            }

            cardViewStudentFragmentDashGlobalTestAssementMap -> {
                Toast("Under Development",true,activity)
            }

            cardViewStudentFragmentDashInbox -> {
                Toast("Under Development",true,activity)
            }




        }

    }


    /***
     * Register Click Event
     */
    private fun registerClickEvent() {
        cardViewStudentFragmentDashLabDirector.setOnClickListener(this)
        cardViewStudentFragmentDashTestMenu.setOnClickListener(this)
        cardViewStudentFragmentDashGlobalTestAssementMap.setOnClickListener(this)
        cardViewStudentFragmentDashInbox.setOnClickListener(this)

    }


}
