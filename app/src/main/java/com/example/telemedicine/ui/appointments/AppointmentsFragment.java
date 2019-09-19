package com.example.telemedicine.ui.appointments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class AppointmentsFragment extends Fragment {
    private AppointmentsViewModel appointmentsViewModel;
    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.LayoutManager layoutManager2;
    private String[] myDataset = {"Item 1", "Item 2", "Item 3"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        appointmentsViewModel =
                ViewModelProviders.of(this).get(AppointmentsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_appointments, container, false);
        final TextView textView = root.findViewById(R.id.text_upcoming_appointments);
        appointmentsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        recyclerView = (RecyclerView)root.findViewById(R.id.recycler);
        recyclerView2 = (RecyclerView)root.findViewById(R.id.recycler2);
        layoutManager = new LinearLayoutManager(this.getActivity());
        layoutManager2 = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView2.setLayoutManager(layoutManager2);
        mAdapter = new RecyclerItem(myDataset);
        recyclerView.setAdapter(mAdapter);
        recyclerView2.setAdapter(mAdapter);
        return root;
    }
}
