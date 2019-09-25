package com.example.telemedicine.ui.appointments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telemedicine.R;
import com.example.telemedicine.ui.sample_pages.sample_appt;
import com.example.telemedicine.ui.utilities.RecyclerItem;
import com.example.telemedicine.ui.utilities.RecyclerItemOld;

public class AppointmentsFragment extends Fragment implements RecyclerItem.OnReportClickListener {
    private AppointmentsViewModel appointmentsViewModel;
    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter mAdapter2;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.LayoutManager layoutManager2;
    private String[] apptData = {"Physical - 9/29 @ 10:00am", "Vaccination - 10/4 @ 1:30pm", "Check-Up - 10/19 @ 9:00am"};
    private String[] apptData2 = {"Wellness Check - 5/19 @ 10:30am", "Eye Exam - 3/22 @ 11:00am", "Check-Up - 2/19 @ 9:00am"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        appointmentsViewModel =
                ViewModelProviders.of(this).get(AppointmentsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_appointments, container, false);
        final TextView textView = root.findViewById(R.id.appt_upcoming_header);
        appointmentsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        recyclerView = (RecyclerView)root.findViewById(R.id.recycler_appt_upcoming);
        recyclerView2 = (RecyclerView)root.findViewById(R.id.recycler_appt_prev);
        layoutManager = new LinearLayoutManager(this.getActivity());
        layoutManager2 = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView2.setLayoutManager(layoutManager2);
        mAdapter = new RecyclerItem(apptData, this);
        mAdapter2 = new RecyclerItem(apptData2, this);
        recyclerView.setAdapter(mAdapter);
        recyclerView2.setAdapter(mAdapter2);
        return root;
    }

    @Override
    public void OnReportClickListener(int position) {
        Intent intent = new Intent(getContext(), sample_appt.class);
        startActivity(intent);
    }
}