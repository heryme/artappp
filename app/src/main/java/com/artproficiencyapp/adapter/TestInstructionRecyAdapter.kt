package com.wus.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import arvaan.dowanloaddemo.comman.SearchFilter
import com.artproficiencyapp.R
import com.flick_trade.model.Album
import kotlinx.android.synthetic.main.item_question_test.view.*


/**
 * Created by Admin on 6-11-2017.
 */
class TestInstructionRecyAdapter(context: Context, var subCategory: ArrayList<Album>) : RecyclerView.Adapter<TestInstructionRecyAdapter.SubViewHolder>() {
    private var searchFilter: SearchFilter? = null
    override fun onBindViewHolder(holder: SubViewHolder?, position: Int) {
        holder?.bind(position,  subCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SubViewHolder {
        return SubViewHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.item_question_test, parent, false))

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun getSelectedList(): List<Album> {
        return subCategory
    }

    override fun getItemCount(): Int = subCategory.size

    inner class SubViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var subCategory: Album? = null
        var context: Context? = null
        fun bind(position: Int,  subCategoryList: ArrayList<Album>) {
            itemView?.rvThirdExceName?.text = subCategoryList[position].name
           // Picasso.with(context).load(subCategoryList[position].image).into(itemView?.cardview_img)

        }
    }


}



