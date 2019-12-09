package com.example.telemedicine.ui.utilities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.telemedicine.R;
import com.example.telemedicine.ui.reports.ReportViewer;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class privacy extends AppCompatActivity {
    private PDFView pdfReport;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        String filename = getIntent().getStringExtra("EXTRA_SESSION_ID");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child(filename);
        pdfReport = (PDFView)findViewById(R.id.privacy_policy);
        Log.d("ReportsDebug", "This is good");

        final long ONE_MEGABYTE = 1024 * 1024;
        storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                pdfReport.fromBytes(bytes).load();
                //Toast.makeText(ReportViewer.this, "Download Successful.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(privacy.this, "Download Failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
