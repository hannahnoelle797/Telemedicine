package com.example.telemedicine.ui.utilities;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.telemedicine.R;

public class RecylerItemBtnDelete extends RecyclerView.Adapter<RecylerItemBtnDelete.MyViewHolder> {
    private String[] data;
    public OnReportClickListener rcl;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView button;
        OnReportClickListener rcl;
        public MyViewHolder(TextView v, OnReportClickListener rclIO){
            super(v);
            button = v;
            rcl = rclIO;
            v.setOnClickListener(this);
        }
        @Override
        public void onClick(View v){
            rcl.OnReportClickListener(getAdapterPosition());
            button.setBackgroundColor(Color.rgb(168, 56, 50));
        }
    }

    public RecylerItemBtnDelete(String[] dataset, OnReportClickListener rclIO){
        data = dataset;
        this.rcl = rclIO;
    }

    public RecylerItemBtnDelete.MyViewHolder onCreateViewHolder(ViewGroup parent, int view){
        TextView item_btn = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_element_button, parent, false);
        MyViewHolder vh = new MyViewHolder(item_btn, rcl);
        return vh;
    }
    public void onBindViewHolder(MyViewHolder h, int pos){
        h.button.setText(data[pos]);
    }
    public int getItemCount(){
        return data.length;
    }
    public interface OnReportClickListener{
        void OnReportClickListener(int position);
    }
}
