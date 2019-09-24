package com.example.telemedicine.ui.reports;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telemedicine.R;
import com.example.telemedicine.ui.utilities.RecyclerItem;


public class ReportsFragment extends Fragment implements RecyclerItem.OnReportClickListener {
    private ReportsViewModel reportsViewModel;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private String[] myDataset = {"Blood Work 9/10", "Vaccination Summary 9/01", "Physical 8/23"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        reportsViewModel =
                ViewModelProviders.of(this).get(ReportsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_reports, container, false);
        final TextView textView = root.findViewById(R.id.reports_header);
        reportsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
                textView.setText("Recent");
            }
        });

        recyclerView = (RecyclerView)root.findViewById(R.id.recycler_reports);
        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new RecyclerItem(myDataset, this);
        recyclerView.setAdapter(mAdapter);

        return root;
    }

    @Override
    public void OnReportClickListener(int position) {
        Toast.makeText(getContext(), myDataset[position], Toast.LENGTH_SHORT).show();
    }
}