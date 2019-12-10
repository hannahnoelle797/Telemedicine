package com.example.telemedicine.ui.video_call;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.telemedicine.R;
import com.example.telemedicine.models.Doctor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class doctor_select extends AppCompatActivity {

    // Globals
    DatabaseReference mDatabaseDocs;
    private ArrayList<String> doctorList;
    RadioGroup doctorGroup;
    RadioButton btn1, btn2, btn3;
    Button submitBtn;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_select);

        // Get references to the Firebase xml items
        doctorGroup = (RadioGroup)findViewById(R.id.doctorSelectionRG);
        submitBtn = (Button)findViewById(R.id.doctorSelectBtn);
        doctorList = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabaseDocs = mDatabase.child("Doctor");
        mAuth = FirebaseAuth.getInstance();

        intent = new Intent(doctor_select.this, video_call.class);

        // Populate the layout with db doctors
        mDatabaseDocs = FirebaseDatabase.getInstance().getReference("Doctor");
        mDatabaseDocs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Doctor docs = child.getValue(Doctor.class);
                    assert docs != null;
                    doctorList.add(docs.getDocString());
                }
                addRadioButtons(doctorList.size());
                for (int i = 0; i < doctorGroup.getChildCount(); i++) {
                    if (doctorList.size() > 0) {
                        ((RadioButton)doctorGroup.getChildAt(i)).setText(doctorList.get(i));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // None selected
                if (doctorGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Please select a doctor...", Toast.LENGTH_SHORT).show();
                } else {
                    int selectedRbId = doctorGroup.getCheckedRadioButtonId();
                    RadioButton selectedRb = (RadioButton)findViewById(selectedRbId);
                    String doctorString = selectedRb.getText().toString();
                    intent.putExtra("docString", doctorString);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    protected void addRadioButtons(int number) {
        for (int row = 0; row < 1; row++) {
            doctorGroup = new RadioGroup(this);
            doctorGroup.setOrientation(LinearLayout.VERTICAL);

            for (int i = 1; i<= number; i++) {
                RadioButton rb = new RadioButton(this);
                rb.setId(View.generateViewId());
                rb.setText("Radio " + rb.getId());
                doctorGroup.addView(rb);
            }
            ((ViewGroup)findViewById(R.id.doctorSelectionRG)).addView(doctorGroup);
        }
    }
}
