package com.artproficiencyapp.test.expandable_list_view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artproficiencyapp.R
import com.artproficiencyapp.test.expandable_list_view.model.CategoryModel
import kotlinx.android.synthetic.main.item_third_category_excepandable.view.*
import android.widget.Filter;
import arvaan.dowanloaddemo.comman.SearchFilter


/**
 * Created by Admin on 6-11-2017.
 */
class ThirdCategoriesAdapter(context: Context, var subCategory: List<CategoryModel.Category_?>) : RecyclerView.Adapter<ThirdCategoriesAdapter.SubViewHolder>() {
    private var searchFilter: SearchFilter? = null

    override fun onBindViewHolder(holder: SubViewHolder?, position: Int) {
        holder?.bind(position, subCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SubViewHolder = SubViewHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.item_third_category_excepandable, parent, false))
    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun getSelectedList(): List<CategoryModel.Category_?> {
        return subCategory
    }

    override fun getItemCount(): Int = subCategory.size
    inner class SubViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var context: Context? = null
        fun bind(position: Int, subCategoryList: List<CategoryModel.Category_?>) {
            itemView?.rvThirdExceName?.text = subCategoryList[position]!!.name
        }

    }
    fun getFilter(): Filter {
        if (searchFilter == null) {
            searchFilter = SearchFilter(subCategory, this)
        }

        return searchFilter as SearchFilter
    }

}





