package com.artproficiencyapp.fragment.director


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.artproficiencyapp.R
import com.artproficiencyapp.activity.TestInstructionActivity
import com.artproficiencyapp.common.FragmentHandler
import com.artproficiencyapp.common.RecyclerTouchListener
import com.artproficiencyapp.fragment.commanfragment.BaseFragment
import com.artproficiencyapp.fragment.commanfragment.PerformanceReportFragment
import com.artproficiencyapp.test.expandable_list_view.adapter.DirectorLabStatisticAdapter
import com.artproficiencyapp.test.expandable_list_view.adapter.ThirdCategoriesAdapter
import com.artproficiencyapp.test.expandable_list_view.model.CategoryModel
import com.artproficiencyapp.test.fragment.TestListFragment
import com.flick_trade.model.Album
import com.flick_trade.model.AlbumLab
import kotlinx.android.synthetic.main.fragment_lab_statistics.*
import kotlinx.android.synthetic.main.fragment_test_list.*


class LabStatisticsFragment : BaseFragment() {
    private var subCategoriesAdapter: DirectorLabStatisticAdapter? = null
    private var albumList: ArrayList<AlbumLab>? = null
    private var objFragmentHandler = FragmentHandler()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lab_statistics, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        albumList = ArrayList()
        albumList!!.add(AlbumLab("Report 1","2:45 AM","14/7/2018"))
        albumList!!.add(AlbumLab("Report 2","3:45 AM","16/7/2018"))
        albumList!!.add(AlbumLab("Report 3","4:45 AM","15/7/2018"))
        albumList!!.add(AlbumLab("Report 4","5:45 AM","17/7/2018"))
        albumList!!.add(AlbumLab("Report 5","6:45 AM","18/7/2018"))
        rcyLabStatistics.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rcyLabStatistics.setHasFixedSize(true)
        setAdapter(albumList!!)



    }
    private fun setAdapter(data: java.util.ArrayList<AlbumLab>) {
        subCategoriesAdapter = DirectorLabStatisticAdapter(activity, data)
        rcyLabStatistics.adapter = subCategoriesAdapter
        rcyLabStatistics.addOnItemTouchListener(RecyclerTouchListener(activity, my_recycler_view, object : RecyclerTouchListener.ClickListener {
            override fun onClick(view: View?, position: Int) {
//                val intent = Intent(activity, TestInstructionActivity::class.java)
//                val bundle = Bundle()
//                bundle.putString("EXTRA_SESSION_ID",   subcategoryList[position]!!.id.toString())
//                intent.putExtras(bundle)
//                activity.startActivity(intent)
                replaceFragment(PerformanceReportFragment(), "Test List", null)
               // showMsgDialog("Under development")
            }
            override fun onLongClick(view: View?, position: Int) {

            }
        }))

    }
    private fun replaceFragment(fragmentFlag: android.app.Fragment, tabName: String, bundle: Bundle?) {

        objFragmentHandler.replaceFragment(activity, R.id.simpleFrameLayout, fragmentFlag, null, bundle, true
                , tabName, 0, FragmentHandler.ANIMATION_TYPE.NONE)
    }
}
