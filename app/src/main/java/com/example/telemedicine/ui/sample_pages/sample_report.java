package com.example.telemedicine.ui.sample_pages;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.telemedicine.R;
import com.github.barteksc.pdfviewer.PDFView;

public class sample_report extends Fragment {
    private PDFView smpl_report;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.sample_report, container, false);

        smpl_report = (PDFView)root.findViewById(R.id.report);

        smpl_report.fromAsset("med_report.pdf").load();

        return root;
    }
}
