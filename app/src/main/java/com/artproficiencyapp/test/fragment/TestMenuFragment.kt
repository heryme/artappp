package com.artproficiencyapp.test.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import com.artproficiencyapp.R
import com.artproficiencyapp.test.expandable_list_view.adapter.FirstLevelExpandListAdapter
import com.artproficiencyapp.test.expandable_list_view.model.CategoryModel
import com.artproficiencyapp.extension.GET_CATEGORIES_LIST
import com.artproficiencyapp.extension.STATUS_CODE
import com.artproficiencyapp.fragment.commanfragment.BaseFragment
import com.artproficiencyapp.restapi.ApiInitialize
import com.artproficiencyapp.restapi.ApiRequest
import com.artproficiencyapp.restapi.ApiResponseInterface
import com.artproficiencyapp.restapi.ApiResponseManager
import kotlinx.android.synthetic.main.fragment_test_menu.*


/**
 * A simple [Fragment] subclass.
 */
class TestMenuFragment : BaseFragment(), ApiResponseInterface {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        setTitle("Test Menu")
        return inflater.inflate(R.layout.fragment_test_menu, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        InitView()
    }


    fun InitView() {
        ApiRequest(activity, ApiInitialize.initialize(ApiInitialize.MAIN_URl_API).getExcepandableList("Bearer " + getUserModel()!!.data.token), GET_CATEGORIES_LIST, true, this)
    }

    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {
            GET_CATEGORIES_LIST -> {
                val categoryModel = apiResponseManager.response as CategoryModel
                when (categoryModel.status) {
                    STATUS_CODE -> {

                        bindAdapter(categoryModel.data)
                    }
//                    else -> Toast(categoryModel.message, false, activity)
                }
//                for (i in 0 until categoryModel.data.size) {
//                    for (j in 0 until categoryModel.data.size) {
//                        Log.e("dddd", "sddd" + categoryModel.data.get(i).category.get(j).name)
//                    }
//                }
//                bindAdapter(categoryModel.data)

            }
        }
    }

    /**
     * Set Expandable Adapter Here
     */
    private fun bindAdapter(data: List<CategoryModel.Datum>) {

        expListFragmentTest!!.setOnGroupExpandListener(object : ExpandableListView.OnGroupExpandListener {
            internal var previousGroup = -1
            override fun onGroupExpand(groupPosition: Int) {
                if (groupPosition != previousGroup)
                    expListFragmentTest!!.collapseGroup(previousGroup)
                previousGroup = groupPosition
            }
        })

        val ExpAdapter = FirstLevelExpandListAdapter(activity, data)
        expListFragmentTest!!.setAdapter(ExpAdapter)
    }
}// Required empty public constructor
