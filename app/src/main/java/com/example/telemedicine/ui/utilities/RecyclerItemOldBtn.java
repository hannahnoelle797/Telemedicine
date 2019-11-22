package com.example.telemedicine.ui.utilities;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.telemedicine.R;

public class RecyclerItemOldBtn extends RecyclerView.Adapter<RecyclerItemOldBtn.MyViewHolder> {
    private String[] mDataset;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;
        public MyViewHolder(TextView v) {
            super(v);
            textView = v;
        }
    }

    public RecyclerItemOldBtn(String[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerItemOldBtn.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_element_button, parent, false);
        RecyclerItemOldBtn.MyViewHolder vh = new RecyclerItemOldBtn.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerItemOldBtn.MyViewHolder holder, int position) {
        holder.textView.setText(mDataset[position]);

    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }

}
