package com.example.telemedicine.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telemedicine.R;
import com.example.telemedicine.ui.utilities.RecyclerItem;
import com.example.telemedicine.ui.utilities.RecyclerItemClickListener;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView_appt, recyclerView_chat, recyclerView_report;
    private RecyclerView.Adapter mAdapter_appt, mAdapter_chat, mAdapter_report;
    private RecyclerView.LayoutManager layoutManager_appt, layoutManager_chat, layoutManager_report;
    private String[] apptData = {"Physical - 9/29 @ 10:00am", "Vaccination - 10/4 @ 1:30pm", "Check-Up - 10/19 @ 9:00am"};
    private String[] chatData = {"Dr. Jane Smith", "Dr. Hayden Lee", "Dr. Michael Dean"};
    private String[] reportData = {"Blood Work 9/10", "Vaccination Summary 9/1", "Physical 8/23"};


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.editText);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        recyclerView_appt = (RecyclerView)root.findViewById(R.id.recycler_appts);
        layoutManager_appt = new LinearLayoutManager(this.getActivity());
        recyclerView_appt.setLayoutManager(layoutManager_appt);
        mAdapter_appt = new RecyclerItem(apptData);
        recyclerView_appt.setAdapter(mAdapter_appt);

        recyclerView_appt.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView_appt ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        recyclerView_chat = (RecyclerView)root.findViewById(R.id.recycler_chats);
        layoutManager_chat = new LinearLayoutManager(this.getActivity());
        recyclerView_chat.setLayoutManager(layoutManager_chat);
        mAdapter_chat = new RecyclerItem(chatData);
        recyclerView_chat.setAdapter(mAdapter_chat);

        recyclerView_chat.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView_chat ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        recyclerView_report = (RecyclerView)root.findViewById(R.id.recycler_reports);
        layoutManager_report = new LinearLayoutManager(this.getActivity());
        recyclerView_report.setLayoutManager(layoutManager_report);
        mAdapter_report = new RecyclerItem(reportData);
        recyclerView_report.setAdapter(mAdapter_report);

        recyclerView_report.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView_report ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        return root;
    }
/*
    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.recycler_item:
                Intent intent = new Intent(HomeFragment.this, AppointmentsFragment.class);
                startActivity(intent);
                break;
            default:
                System.out.println("Default");
        }
    }*/
}