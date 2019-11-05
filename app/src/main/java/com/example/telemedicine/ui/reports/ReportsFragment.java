package com.example.telemedicine.ui.reports;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telemedicine.R;
import com.example.telemedicine.models.Report;
import com.example.telemedicine.ui.utilities.RecyclerItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;


public class ReportsFragment extends Fragment implements RecyclerItem.OnReportClickListener {
    private DatabaseReference databaseReports;
    private ReportsViewModel reportsViewModel;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<Report> reportsList;
    private String[] reportNames;

    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        reportsViewModel =
                ViewModelProviders.of(this).get(ReportsViewModel.class);
        root = inflater.inflate(R.layout.fragment_reports, container, false);
        final TextView textView = root.findViewById(R.id.reports_header);
        reportsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
                textView.setText("Recent");
            }
        });

        reportNames = new String[1];
        reportNames[0] = "No Reports Found.";
        reportsList = new ArrayList<>();
        databaseReports = FirebaseDatabase.getInstance().getReference("Reports");
        String id = UUID.randomUUID().toString();
        //String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //databaseReports.child(id).setValue(new Report(id, "2ditset", "hospital_medical_report", "https://firebasestorage.googleapis.com/v0/b/telemedicine-capstone.appspot.com/o/Hospital_Medical_Report.pdf?alt=media&token=2cc77b00-3bd0-4f8a-9516-2752bae649e3"));
        databaseReports.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot reportSnapshot: dataSnapshot.getChildren())
                {
                    Report report = reportSnapshot.getValue(Report.class);
                    //if(report.getUserID().equals("ditset")) { //get reports with matching user id
                    reportsList.add(report);
                    //}
                }

                reportNames = new String[reportsList.size()];

                if(reportsList.size() > 0) {
                    for (int i = 0; i < reportNames.length; i++) {
                        reportNames[i] = reportsList.get(i).getReportName();
                    }
                }
                populateRecycler(root);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ReportsFirebase", "Data retrieval cancelled");
            }
        });

        return root;
    }

    public void populateRecycler(View root)
    {
        recyclerView = (RecyclerView)root.findViewById(R.id.recycler_reports);
        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new RecyclerItem(reportNames, this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void OnReportClickListener(int position) {
        Intent intent = new Intent(getContext(), ReportViewer.class);
        //send url as extra
        intent.putExtra("reportURL" ,reportsList.get(position).getUrl());
        startActivity(intent);
    }
}