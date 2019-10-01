package com.example.telemedicine.ui.utilities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.telemedicine.R;

public class RecyclerItem extends RecyclerView.Adapter<RecyclerItem.MyViewHolder> {
    private String[] mDataset;
    private OnReportClickListener mReportClickListener;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView textView;
        OnReportClickListener reportClickListener;

        public MyViewHolder(View v,OnReportClickListener reportClickListener) {
            super(v);
            textView = v.findViewById(R.id.recycler_item);
            this.reportClickListener = reportClickListener;

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            reportClickListener.OnReportClickListener(getAdapterPosition());
        }
    }

    public RecyclerItem(String[] myDataset, OnReportClickListener onReportClickListener) {
        mDataset = myDataset;
        this.mReportClickListener = onReportClickListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerItem.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_element, parent, false);
        MyViewHolder vh = new MyViewHolder(v, mReportClickListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.textView.setText(mDataset[position]);

    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    public interface OnReportClickListener{
        void OnReportClickListener(int position);
    }
}