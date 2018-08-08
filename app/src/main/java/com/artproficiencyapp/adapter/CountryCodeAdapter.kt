package com.artproficiencyapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.artproficiencyapp.R
import com.artproficiencyapp.common.RegularTextview
import com.artproficiencyapp.model.CountryCodeModel
import java.util.*

/**
 * Created by admin on 08-Jul-17.
 */

class CountryCodeAdapter(context: Context, val resource: Int, val objects: ArrayList<CountryCodeModel>?) : ArrayAdapter<CountryCodeModel>(context, resource, objects) {

    override fun getCount(): Int {
        return objects!!.size
    }

    override fun getItem(i: Int): CountryCodeModel? {
        return objects!![i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
        val view = (LayoutInflater.from(context).inflate(R.layout.row_country_code, viewGroup, false))
        val tvCountryCode = view!!.findViewById<RegularTextview>(R.id.tv_country_code)
        tvCountryCode.text = objects!![i]._mCountryCode
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = (LayoutInflater.from(context).inflate(R.layout.row_country_code_name, parent, false))

        val tvCountryName = convertView!!.findViewById<RegularTextview>(R.id.row_tv_country_name)
        val tvCountryCode = convertView.findViewById<RegularTextview>(R.id.row_tv_country_code)

        tvCountryCode.text = objects!![position]._mCountryCode
        tvCountryName.text = objects[position]._mCountryName
        return convertView
    }
}
