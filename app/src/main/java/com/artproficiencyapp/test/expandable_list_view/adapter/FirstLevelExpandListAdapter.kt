package com.artproficiencyapp.test.expandable_list_view.adapter

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView

import com.artproficiencyapp.R
import com.artproficiencyapp.test.expandable_list_view.model.CategoryModel
import com.artproficiencyapp.test.expandable_list_view.utils.CustomExpandableListview
import com.artproficiencyapp.test.fragment.TestListFragment
import com.artproficiencyapp.common.FragmentHandler

import java.util.ArrayList
import com.artproficiencyapp.activity.TestInstructionActivity
import java.io.Serializable


class FirstLevelExpandListAdapter(private val context: Context, private val groups: List<CategoryModel.Datum>) : BaseExpandableListAdapter() {
    private var lastExpandedPosition = -1
    private var objFragmentHandler = FragmentHandler()
    private var listdata: List<CategoryModel.Category_>? = null
    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        val chList = groups[groupPosition].category[groupPosition].category
        // Log.e("Frist", "Frist adapter child size ====  " + chList.size() + " name  " + chList.get(groupPosition).getName());
        return chList[childPosition]
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        val list = ArrayList<CategoryModel.Category>()
        // Log.e("", " frist adapter child postion return====    " + childPosition);
        return childPosition.toLong()
    }

    override fun getChildView(p0: Int, p1: Int, p2: Boolean, p3: View?, p4: ViewGroup?): View {
        val customExpandableListview = CustomExpandableListview(context)
        customExpandableListview.setAdapter(SecondLevelExpandListAdapter(context, groups[p0].category))
        customExpandableListview.setGroupIndicator(null)
        customExpandableListview.divider = context.resources.getDrawable(R.drawable.child_expandable_separator)

        customExpandableListview.setOnGroupClickListener { parent, v, groupPosition, id ->
            Log.e("GroupClicked", " Position = " + groupPosition)


            val childCountWay2 = parent.expandableListAdapter.getChildrenCount(groupPosition)
            if (childCountWay2 < 1) {

               // context.startActivity(Intent(context, TestInstructionActivity::class.java))
                val intent = Intent(context, TestInstructionActivity::class.java)
//                intent.putExtra("EXTRA_SESSION_ID", groups[p0].category[groupPosition].id.toString())
//                context.startActivity(intent)

                val bundle = Bundle()
                bundle.putString("EXTRA_SESSION_ID",  groups[p0].category[groupPosition].id.toString())
                intent.putExtras(bundle)
                context.startActivity(intent)


            } else {
                listdata = groups[p0].category[groupPosition].category
                val bundle = Bundle()
                bundle.putSerializable("model", listdata as Serializable)
                bundle.putString("EXTRA_SESSION_ID",  groups[p0].category[groupPosition].id.toString())
                replaceFragment(TestListFragment(), "Test List", bundle)
            }

            false
        }




        return customExpandableListview
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        val chList = groups[groupPosition].category
        //      Log.e("Childre", "Frist Adapter children count ====" + chList.size());
        return 1
    }

    override fun getGroup(groupPosition: Int): Any {
        //    Log.e("Group", "Frist adapter Gruop count=== " + groups.get(groupPosition).getCategoryType());
        return groups[groupPosition]
    }

    override fun getGroupCount(): Int {
        //     Log.e("Group", "Frist adapter Gruop count== " + groups.size());
        return groups.size
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }



    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, p3: ViewGroup?): View {
        var convertView = convertView
        val group = getGroup(groupPosition) as CategoryModel.Datum
        if (convertView == null) {
            val inf = context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inf.inflate(R.layout.row_expanable_first_level, null)
        }

        val tv = convertView!!.findViewById<View>(R.id.rowSecondText) as TextView
        val ivRowExpanableFirstLevel = convertView.findViewById<View>(R.id.ivRowExpanableFirstLevel) as ImageView
        tv.text = group.categoryType


        if (isExpanded) {
            ivRowExpanableFirstLevel.setImageResource(R.drawable.excepandable_up)
        } else {
            ivRowExpanableFirstLevel.setImageResource(R.drawable.excepandable_down)
        }

        return convertView
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    private fun replaceFragment(fragmentFlag: Fragment, tabName: String, bundle: Bundle?) {

        objFragmentHandler.replaceFragment(context as Activity, R.id.simpleFrameLayout, fragmentFlag, null, bundle, true
                , tabName, 0, FragmentHandler.ANIMATION_TYPE.NONE)
    }

}