package com.example.telemedicine.ui.video_call;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.telemedicine.R;
import com.example.telemedicine.models.Doctor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * @author - David Howard
 */
public class doctor_select extends AppCompatActivity {

    // Globals
    DatabaseReference mDatabaseDocs;
    private ArrayList<String> doctorList;
    RadioGroup doctorGroup;
    RadioButton btn1, btn2, btn3;
    Button submitBtn;
    String docId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_select);

        // Get references to the Firebase xml items
        doctorGroup = (RadioGroup)findViewById(R.id.doctorSelectionRG);
        btn1 = (RadioButton)findViewById(R.id.doctorOneRB);
        btn2 = (RadioButton)findViewById(R.id.doctorTwoRB);
        btn3 = (RadioButton)findViewById(R.id.doctorThreeRB);
        submitBtn = (Button)findViewById(R.id.doctorSelectBtn);
        doctorList = new ArrayList<>();
        // Init docId to 0
        docId = "0";

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
                    Intent intent;
                    int selectedRbId = doctorGroup.getCheckedRadioButtonId();
                    RadioButton selectedRb = (RadioButton)findViewById(selectedRbId);
                    String doctorString = selectedRb.getText().toString();
                    intent = new Intent(doctor_select.this, video_call.class);
                    // Assign doctor id to video_call statically
                    getDocId(doctorString);
                    // Start video call class
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    /**
     * Called to get the correct doctor id from the db
     * @param docName - The selected doctor (String)
     */
    protected void getDocId(final String docName) {
        mDatabaseDocs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Doctor docs = child.getValue(Doctor.class);
                    assert docs != null;
                    if (docName.equals(docs.getDocString())) {
                        docId = docs.getDocID();
                        video_call.accessDocId(docId);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
