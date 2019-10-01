package com.example.telemedicine.ui.sample_pages;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.telemedicine.R;
import com.github.barteksc.pdfviewer.PDFView;

public class sample_report extends AppCompatActivity {
    private PDFView smpl_report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_report);
        smpl_report = (PDFView)findViewById(R.id.report);
        smpl_report.fromAsset("med_report.pdf").load();
    }
}
