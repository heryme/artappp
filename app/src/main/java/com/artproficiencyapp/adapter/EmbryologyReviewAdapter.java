package com.artproficiencyapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.artproficiencyapp.R;

import java.util.List;

public class EmbryologyReviewAdapter extends RecyclerView.Adapter<EmbryologyReviewAdapter.MyViewHolder> {

    private List<String> feedbackList;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvEmbryologyReviewAdapterFeedbackName;

        public MyViewHolder(View view) {
            super(view);
            tvEmbryologyReviewAdapterFeedbackName = (TextView) view.findViewById(R.id.tvEmbryologyReviewAdapterFeedbackName);
        }
    }


    public EmbryologyReviewAdapter(List<String> feedbackList) {
        this.feedbackList = feedbackList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_feedback_embryology, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvEmbryologyReviewAdapterFeedbackName.setText(feedbackList.get(position));
    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }
}


