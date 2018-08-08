package com.artproficiencyapp.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.view.PagerAdapter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast

import com.artproficiencyapp.R
import com.artproficiencyapp.activity.TestInstructionActivity
import com.artproficiencyapp.test.activity.PhotoEditorActivity
import com.bumptech.glide.Glide

import java.util.ArrayList

class QuestionImagePagerAdapter(private val context: Context, private val IMAGES: ArrayList<String>) : PagerAdapter() {
    private val inflater: LayoutInflater


    init {
        inflater = LayoutInflater.from(context)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return IMAGES.size
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val imageLayout = inflater.inflate(R.layout.row_question_image_pager, view, false)!!

        val imageView = imageLayout.findViewById<View>(R.id.ivRowQuestionImagePagerLoader) as ImageView
//        val edit = imageLayout.findViewById<View>(R.id.btnMultipleRowQuestionImageEdit) as Button
//        edit.setOnClickListener {
//           // Toast.makeText(context,"click==>"+IMAGES[position],Toast.LENGTH_LONG).show();
//          //  Log.e("","Imageeeeeee"+IMAGES[position])
//
//
//            val intent = Intent(context, PhotoEditorActivity::class.java)
//            val bundle = Bundle()
//            bundle.putString("Edit_Image",  IMAGES[position])
//            intent.putExtras(bundle)
//            context.startActivity(intent)
//
//        }

       // imageView.setImageResource(IMAGES[position])
        Glide.with(context).load(IMAGES[position])
                .thumbnail(0.5f)
                .into(imageView)
        view.addView(imageLayout, 0)
        return imageLayout
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}

    override fun saveState(): Parcelable? {
        return null
    }

}
