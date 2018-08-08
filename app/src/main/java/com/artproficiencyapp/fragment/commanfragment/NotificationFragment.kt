package com.artproficiencyapp.fragment.commanfragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.artproficiencyapp.R
import com.artproficiencyapp.adapter.NotificationAdapter
import com.artproficiencyapp.common.RecyclerTouchListener
import com.artproficiencyapp.fragment.commanfragment.BaseFragment
import com.artproficiencyapp.test.expandable_list_view.adapter.DirectorLabStatisticAdapter
import com.flick_trade.model.AlbumLab
import kotlinx.android.synthetic.main.fragment_director_inbox.*
import kotlinx.android.synthetic.main.fragment_notification.*
import kotlinx.android.synthetic.main.fragment_test_list.*


class NotificationFragment : BaseFragment() {
    private var subCategoriesAdapter: NotificationAdapter? = null
    private var albumList: ArrayList<AlbumLab>? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        albumList = ArrayList()
        albumList!!.add(AlbumLab("It is radio_button_selected_state long established fact that radio_button_selected_state reader will be distracted by the readable conten ","https://images.idgesg.net/images/article/2017/08/android_robot_logo_by_ornecolorada_cc0_via_pixabay1904852_wide-100732483-large.jpg",""))
        albumList!!.add(AlbumLab("Lorem Ipsum is simply dummy text of the printing and typesetting industry","https://2.bp.blogspot.com/-2ZMkSo7CnUs/WvMvSK0u9RI/AAAAAAAAFZA/zJOCZ8LUM8ol3hcHYHwVyOpc3iiYaxquACLcBGAs/s1600/Jetpack_logo.png",""))
        albumList!!.add(AlbumLab("Lorem Ipsum is simply dummy text of the printing and typesetting industry","https://cdn1.techadvisor.co.uk/cmsdata/features/3607615/Android_thumb800.jpg","Hello"))
        albumList!!.add(AlbumLab("Lorem Ipsum is simply dummy text","https://cdn57.androidauthority.net/wp-content/uploads/2016/06/android-win-2-300x162.png","Hello"))
        albumList!!.add(AlbumLab("Lorem Ipsum is simply dummy text of the printing and typesetting industry","https://edge.alluremedia.com.au/m/l/2017/06/Android.jpg","Maza"))
        albumList!!.add(AlbumLab("Lorem Ipsum is simply dummy text of the printing and typesetting industry","https://images.idgesg.net/images/article/2017/08/android_robot_logo_by_ornecolorada_cc0_via_pixabay1904852_wide-100732483-large.jpg","Hiii"))
        albumList!!.add(AlbumLab("Lorem Ipsum is simply dummy text of the printing and typesetting industry","https://2.bp.blogspot.com/-2ZMkSo7CnUs/WvMvSK0u9RI/AAAAAAAAFZA/zJOCZ8LUM8ol3hcHYHwVyOpc3iiYaxquACLcBGAs/s1600/Jetpack_logo.png","What are doing"))
        albumList!!.add(AlbumLab("Lorem Ipsum is simply dummy text of the printing and typesetting industry","https://cdn1.techadvisor.co.uk/cmsdata/features/3607615/Android_thumb800.jpg","Hello"))
        albumList!!.add(AlbumLab("Lorem Ipsum is simply dummy text of the printing ","https://cdn57.androidauthority.net/wp-content/uploads/2016/06/android-win-2-300x162.png","Hello"))
        albumList!!.add(AlbumLab("Lorem Ipsum is simply dummy text of the printing and typesetting industry","https://edge.alluremedia.com.au/m/l/2017/06/Android.jpg","Maza"))
        rcyDirectorNotification.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rcyDirectorNotification.setHasFixedSize(true)
        setAdapter(albumList!!)
    }
    private fun setAdapter(data: java.util.ArrayList<AlbumLab>) {
        subCategoriesAdapter = NotificationAdapter(activity, data)
        rcyDirectorNotification.adapter = subCategoriesAdapter
        rcyDirectorNotification.addOnItemTouchListener(RecyclerTouchListener(activity, my_recycler_view, object : RecyclerTouchListener.ClickListener {
            override fun onClick(view: View?, position: Int) {
//                val intent = Intent(activity, TestInstructionActivity::class.java)
//                val bundle = Bundle()
//                bundle.putString("EXTRA_SESSION_ID",   subcategoryList[position]!!.id.toString())
//                intent.putExtras(bundle)
//                activity.startActivity(intent)
                showMsgDialog("Under development")
            }
            override fun onLongClick(view: View?, position: Int) {

            }
        }))

    }
}
