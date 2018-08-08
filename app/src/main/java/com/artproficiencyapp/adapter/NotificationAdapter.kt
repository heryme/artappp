package com.artproficiencyapp.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.artproficiencyapp.R
import arvaan.dowanloaddemo.comman.SearchFilter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.flick_trade.model.AlbumLab
import kotlinx.android.synthetic.main.item_row_director_inbox.view.*
import kotlinx.android.synthetic.main.item_row_labstatistics_director.view.*
import kotlinx.android.synthetic.main.item_row_notification.view.*


/**
 * Created by Admin on 6-11-2017.
 */
class NotificationAdapter(var context: Context, var subCategory: List<AlbumLab>) : RecyclerView.Adapter<NotificationAdapter.SubViewHolder>() {
    private var searchFilter: SearchFilter? = null

    override fun onBindViewHolder(holder: SubViewHolder?, position: Int) {
        holder?.bind(position, subCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SubViewHolder = SubViewHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.item_row_notification, parent, false))
    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun getSelectedList(): List<AlbumLab> {
        return subCategory
    }

    override fun getItemCount(): Int = subCategory.size
    inner class SubViewHolder(view: View) : RecyclerView.ViewHolder(view) {
       // var context: Context? = null
        fun bind(position: Int, subCategoryList: List<AlbumLab>) {

            Glide.with(context).load(subCategoryList[position]!!.time)
                    .into(itemView.imgItemRowINotification);
            itemView?.txtItemRowNotificationName?.text = subCategoryList[position]!!.name


        }

    }


}





