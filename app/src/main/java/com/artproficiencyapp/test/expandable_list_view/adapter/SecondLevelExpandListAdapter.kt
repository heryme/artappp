package com.artproficiencyapp.test.expandable_list_view.adapter

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView


import com.artproficiencyapp.R
import com.artproficiencyapp.test.expandable_list_view.model.CategoryModel

class SecondLevelExpandListAdapter(private val context: Context, private val groups: List<CategoryModel.Category>) : BaseExpandableListAdapter() {

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        val chList = groups[groupPosition].category
        // Log.e("Child postion", "child postion" + childPosition);
        return chList[childPosition]
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        // Log.e("", "return====    " + childPosition);
        return childPosition.toLong()
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        val child = getChild(groupPosition, childPosition) as CategoryModel.Category_
        if (convertView == null) {
            val infalInflater = context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = infalInflater.inflate(R.layout.row_expanable_second_level, null)
        }
       // val tv = convertView!!.findViewById<View>(R.id.rowExpanableFirstLevelHeaderName) as TextView
       // tv.text = child.name



        return convertView!!
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        val chList = groups[groupPosition].category
        return chList?.size ?: 0


    }

    override fun getGroup(groupPosition: Int): Any {
        //Log.e("Group", "second adapter Gruop count==  " + groupPosition);
        return groups[groupPosition]
    }

    override fun getGroupCount(): Int {
        //  Log.e("Group", "second adapter Gruop count==  " + groups.size());
        return groups.size
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        val datum = getGroup(groupPosition) as CategoryModel.Category
        if (convertView == null) {
            val inf = context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inf.inflate(R.layout.row_third, null)
        }
        val tv = convertView!!.findViewById<View>(R.id.row_expanable_third_level) as TextView
        tv.text = datum.name



        return convertView



    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }


}