package com.artproficiencyapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.artproficiencyapp.R;

import java.lang.reflect.Array;
import java.util.List;

public class PerformaceReportDataAdapter extends RecyclerView.Adapter<PerformaceReportDataAdapter.MyViewHolder> {

    private List<String> reportDataList;
    //private int colorArray[];
    private List<Integer> colorList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvRowPerformanceReportData,
                tvRowPerformanceReportDataPercentage;
        public View viewRowPerformanceReportColor;

        public MyViewHolder(View view) {
            super(view);
            tvRowPerformanceReportData = (TextView) view.findViewById(R.id.tvRowPerformanceReportData);
            tvRowPerformanceReportDataPercentage = (TextView) view.findViewById(R.id.tvRowPerformanceReportDataPercentage);
            viewRowPerformanceReportColor = (View) view.findViewById(R.id.viewRowPerformanceReportColor);
        }
    }


    public PerformaceReportDataAdapter(List<String> reportDataList,List<Integer> colorList) {
        this.reportDataList = reportDataList;
        this.colorList = colorList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_performance_report_details, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvRowPerformanceReportData.setText("Center"/*reportDataList.get(position)*/);
        holder.tvRowPerformanceReportDataPercentage.setText(reportDataList.get(position));
        holder.viewRowPerformanceReportColor.setBackgroundColor(colorList.get(position));
    }

    @Override
    public int getItemCount() {
        return reportDataList.size();
    }
}


