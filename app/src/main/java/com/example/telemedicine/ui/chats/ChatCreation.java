package com.example.telemedicine.ui.chats;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.telemedicine.R;
import com.example.telemedicine.models.Doctor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatCreation extends AppCompatActivity {
    private Spinner doctorSpinner;
    private ArrayList<String> doctorNames;
    private ArrayList<String> doctorIds;
    private DatabaseReference dbDoctor;
    private DatabaseReference dbChat;
    private DatabaseReference dbUser;
    private ArrayList<String> chatIds;
    private int index;
    Context con;
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_chat_creation);
        con = this;
        populateDoctor();

    }
    public void populateDoctor(){
        doctorSpinner = findViewById(R.id.doctor_spinner);
        dbDoctor = FirebaseDatabase.getInstance().getReference("Doctor");
        doctorNames = new ArrayList<>();
        doctorIds = new ArrayList<>();
        doctorNames.add(" ");
        doctorIds.add(" ");
        dbDoctor.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot child : dataSnapshot.getChildren()){
                    Doctor d = child.getValue(Doctor.class);
                    doctorNames.add(d.getDocString());
                    doctorIds.add(d.getDocID());
                }

                populateDocSpinner(doctorNames);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void populateDocSpinner(ArrayList<String> doctorNames){
        String[] docNames = doctorNames.toArray(new String[doctorNames.size()]);
        ArrayAdapter<String> doc_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, docNames);
        doctorSpinner.setAdapter(doc_adapter);
        doctorSpinner.setVisibility(View.VISIBLE);
        final ArrayList<String> dm = doctorNames;
        doctorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String s = doctorSpinner.getSelectedItem().toString();
                if(!s.equals(" ")) {
                    for (int j = 0; j < dm.size(); j++) {
                        if (dm.get(j).equals(s)) {
                            index = j;
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
