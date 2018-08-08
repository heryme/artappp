package com.artproficiencyapp.fragment.commanfragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.artproficiencyapp.R
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.charts.PieChart
import kotlinx.android.synthetic.main.fragment_performance_report.*
import android.graphics.Color
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import com.artproficiencyapp.adapter.PerformaceReportDataAdapter
import com.artproficiencyapp.extension.DIRECTOR
import java.util.*


class PerformanceReportFragment : BaseFragment() {
    private var pieChart: PieChart? = null
    private var entries: ArrayList<Entry>? = null
    private var PieEntryLabels: ArrayList<String>? = null
    private var pieDataSet: PieDataSet? = null
    private var pieData: PieData? = null
    private var performanceReportDataAdapter: PerformaceReportDataAdapter? = null
    private var perfomanceReportDataList: MutableList<String>? = null
    private var colorList: List<Int>? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_performance_report, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (getUserModel()!!.data.user_type== DIRECTOR) {
            setTitle("Report Details")
        }
        entries = ArrayList()

        //Set Adapter
        setAdapter()
        addValuesToPieEntry()

        pieDataSet = PieDataSet(entries, "")

        pieData = PieData(perfomanceReportDataList, pieDataSet)

        pieDataSet!!.setColors(colorList)

        chartPerformanceReportFragment.setData(pieData)

        //Remove Default Data From The Pie Chart
        chartPerformanceReportFragment.getLegend().setEnabled(false);
        chartPerformanceReportFragment.setDrawSliceText(false)
        chartPerformanceReportFragment.setDrawMarkerViews(false)
        pieData?.setDrawValues(false)
        chartPerformanceReportFragment.setDescription("")
        chartPerformanceReportFragment.animateY(3000)
    }

    fun addValuesToPieEntry() {
        entries!!.add(BarEntry(23f, 0))
        entries!!.add(BarEntry(17F, 1))
        entries!!.add(BarEntry(10F, 2))
    }

    /**
     * Set Adapter
     */
    fun setAdapter() {
        perfomanceReportDataList = ArrayList()
        colorList = ArrayList()
        perfomanceReportDataList!!.add("23%")
        perfomanceReportDataList!!.add("17%")
        perfomanceReportDataList!!.add("10%")

        //Generate color
        for (i in 0 until (perfomanceReportDataList as ArrayList<String>).size) {
            //colorList.add()
            val rnd = Random()
            (colorList as ArrayList<Int>).add( Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)))
        }

        performanceReportDataAdapter = PerformaceReportDataAdapter(perfomanceReportDataList,colorList)
        rvPerformanceReportFragmentData.setLayoutManager(GridLayoutManager(activity, 3))
        rvPerformanceReportFragmentData.setItemAnimator(DefaultItemAnimator())
        rvPerformanceReportFragmentData.setAdapter(performanceReportDataAdapter)
    }

}
