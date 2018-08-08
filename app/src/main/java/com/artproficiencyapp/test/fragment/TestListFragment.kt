package com.artproficiencyapp.test.fragment


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.artproficiencyapp.R
import com.artproficiencyapp.activity.TestInstructionActivity
import com.artproficiencyapp.common.RecyclerTouchListener

import com.artproficiencyapp.test.expandable_list_view.adapter.ThirdCategoriesAdapter
import com.artproficiencyapp.test.expandable_list_view.model.CategoryModel
import com.artproficiencyapp.fragment.commanfragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_test_list.*
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class TestListFragment : BaseFragment(), android.support.v7.widget.SearchView.OnQueryTextListener {
    override fun onQueryTextSubmit(query: String?): Boolean {
        Log.e("","Valueeee"+query)
        subCategoriesAdapter?.getFilter()?.filter(query)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        Log.e("","Valueeee"+newText)
        subCategoriesAdapter?.getFilter()?.filter(newText)
       return  true
    }



    private var subCategoriesAdapter: ThirdCategoriesAdapter? = null
    private var categoriesList: ArrayList<CategoryModel>? = null
    var subcategoryList = ArrayList<CategoryModel.Category_?>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        setTitle("Test List")
       // setToolbarImage(R.drawable.back_icon)
        return inflater.inflate(R.layout.fragment_test_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        inItView()
        searchViewFragmentTestList.setOnQueryTextListener(this)
    }

    private fun inItView() {

        subcategoryList = arguments.getSerializable("model") as ArrayList<CategoryModel.Category_?>
        my_recycler_view.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        my_recycler_view.setHasFixedSize(true)
        setAdapter(subcategoryList)

    }


    private fun setAdapter(data: java.util.ArrayList<CategoryModel.Category_?>) {
        subCategoriesAdapter = ThirdCategoriesAdapter(activity, data)
        my_recycler_view.adapter = subCategoriesAdapter
        my_recycler_view.addOnItemTouchListener(RecyclerTouchListener(activity, my_recycler_view, object : RecyclerTouchListener.ClickListener {
            override fun onClick(view: View?, position: Int) {
                val intent = Intent(activity, TestInstructionActivity::class.java)
                val bundle = Bundle()
                bundle.putString("EXTRA_SESSION_ID",   subcategoryList[position]!!.id.toString())
                intent.putExtras(bundle)
                activity.startActivity(intent)
            }
            override fun onLongClick(view: View?, position: Int) {

            }
        }))

    }




}// Required empty public constructor
