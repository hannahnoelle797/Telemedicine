package com.example.telemedicine.ui.appointments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.content.Intent;

import androidx.fragment.app.FragmentTransaction;
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

        recyclerView = (RecyclerView)root.findViewById(R.id.recycler);
        recyclerView2 = (RecyclerView)root.findViewById(R.id.recycler2);
        layoutManager = new LinearLayoutManager(this.getActivity());
        layoutManager2 = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView2.setLayoutManager(layoutManager2);
        mAdapter = new RecyclerItem(apptData);
        mAdapter2 = new RecyclerItem(apptData2);
        recyclerView.setAdapter(mAdapter);
        recyclerView2.setAdapter(mAdapter2);

        Button btnFragment = root.findViewById(R.id.btn_schedule_appt);
        btnFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                //getFragmentManager().beginTransaction().add(R.id.appt_details_Fragment, new AppointmentsFragment()).commit();
            }
        });
        return root;
    }
}
