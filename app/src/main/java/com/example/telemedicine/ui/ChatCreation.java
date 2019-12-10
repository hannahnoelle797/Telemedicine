package com.example.telemedicine.ui.chats;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.telemedicine.MainActivity;
import com.example.telemedicine.R;
import com.example.telemedicine.models.Chat;
import com.example.telemedicine.models.Doctor;
import com.example.telemedicine.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class ChatCreation extends AppCompatActivity {
    private Spinner doctorSpinner;
    private ArrayList<String> doctorNames;
    private ArrayList<String> doctorIds;
    private ArrayList<String> patientNames;
    private ArrayList<String> patientIds;
    private DatabaseReference dbDoctor;
    private DatabaseReference dbChat;
    private DatabaseReference dbUser;
    private ArrayList<String> chatIds;
    private int index;
    private String userId;
    private String userName;
    private boolean isDoctor=false;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dbDoctor = FirebaseDatabase.getInstance().getReference("Doctor");
        dbChat = FirebaseDatabase.getInstance().getReference("Chat");
        dbUser = FirebaseDatabase.getInstance().getReference("Users");
        dbDoctor.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot child: dataSnapshot.getChildren()){
                    Doctor d = child.getValue(Doctor.class);
                    if(d.getDocID().equals(userId)) isDoctor = true;
                }
                if(isDoctor){
                    setContentView(R.layout.activity_chat_creation_doctor);
                    dbDoctor.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot child : dataSnapshot.getChildren()){
                                Doctor d = child.getValue(Doctor.class);
                                if(d.getDocID().equals(userId)){
                                    userName = d.getDocString();
                                }
                            }
                            populatePatient();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }else{
                    setContentView(R.layout.activity_chat_creation);
                    dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot child : dataSnapshot.getChildren()){
                                User u = child.getValue(User.class);
                                if (u.getUserID().equals(userId)) {
                                    userName = (u.getFullName());
                                }
                            }
                            populateDoctor();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
    public void populatePatient(){
        doctorSpinner = findViewById(R.id.patient_spinner);
        patientNames = new ArrayList<>();
        patientIds = new ArrayList<>();
        patientNames.add(" ");
        patientIds.add(" ");
        dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    User u = child.getValue(User.class);
                    patientNames.add(u.getFullName());
                    patientIds.add(u.getUserID());
                }
                dbChat.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            Chat c = child.getValue(Chat.class);
                            int i = 0;
                            while(i<patientIds.size()) {
                                if ((c.getPatientId().equals(patientIds.get(i))&& c.getDoctorId().equalsIgnoreCase(userId)) && c.isStatus()) {
                                    patientIds.remove(i);
                                    patientNames.remove(i);
                                } else {
                                    i++;
                                }
                            }
                        }
                        populatePatientSpinner(patientNames);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    public void populateDoctor() {
        doctorSpinner = findViewById(R.id.doctor_spinner);
        doctorNames = new ArrayList<>();
        doctorIds = new ArrayList<>();
        doctorNames.add(" ");
        doctorIds.add(" ");
        dbDoctor.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Doctor d = child.getValue(Doctor.class);
                    doctorNames.add(d.getDocString());
                    doctorIds.add(d.getDocID());
                }
                dbChat.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            Chat c = child.getValue(Chat.class);
                            int i = 0;
                            while(i<doctorIds.size()) {
                                if ((c.getDoctorId().equals(doctorIds.get(i)) && c.getPatientId().equalsIgnoreCase(userId)) && c.isStatus()) {
                                    doctorIds.remove(i);
                                    doctorNames.remove(i);
                                } else {
                                    i++;

                                }
                            }
                        }
                        populateDocSpinner(doctorNames);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    public void populatePatientSpinner(ArrayList<String> patientNames){
        if(patientNames.size()==1){
            Toast.makeText(ChatCreation.this, "No patients Available", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(ChatCreation.this, MainActivity.class);
            startActivity(i);
        }
        String patNames[] = patientNames.toArray(new String[patientNames.size()]);
        ArrayAdapter<String> pat_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, patNames);
        doctorSpinner.setAdapter(pat_adapter);
        doctorSpinner.setVisibility(View.VISIBLE);
        final ArrayList<String> dm = patientNames;
        doctorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String s = doctorSpinner.getSelectedItem().toString();
                if(!s.equals(" ")){
                    for(int j=0;j<dm.size();j++){
                        if(dm.get(j).equals(s)){
                            index = j;
                        }
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        dbChat = FirebaseDatabase.getInstance().getReference();
        dbChat.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("Chat")) {
                    dbChat = FirebaseDatabase.getInstance().getReference("Chat");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void populateDocSpinner(ArrayList<String> doctorNames) {
        if(doctorNames.size()==1){
            Toast.makeText(ChatCreation.this, "No Doctors Available", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(ChatCreation.this, MainActivity.class);
            startActivity(i);
        }
        String[] docNames = doctorNames.toArray(new String[doctorNames.size()]);
        ArrayAdapter<String> doc_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, docNames);
        doctorSpinner.setAdapter(doc_adapter);
        doctorSpinner.setVisibility(View.VISIBLE);
        final ArrayList<String> dm = doctorNames;
        doctorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String s = doctorSpinner.getSelectedItem().toString();
                if (!s.equals(" ")) {
                    for (int j = 0; j < dm.size(); j++) {
                        if (dm.get(j).equals(s)) {
                            index = j;
                        }
                    }
                }
                dbChat = FirebaseDatabase.getInstance().getReference();
                dbChat.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("Chat")) {
                            dbChat = FirebaseDatabase.getInstance().getReference("Chat");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void btnClicked(View v) {
        switch (v.getId()) {
            case R.id.button_new_chat:
                if (doctorSpinner.getSelectedItem().equals(" ")) {
                    Toast.makeText(ChatCreation.this, "Please Select a Doctor", Toast.LENGTH_SHORT).show();
                } else {
                    createChat();
                    Toast.makeText(ChatCreation.this, "Chat Created", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(ChatCreation.this, MainActivity.class);
                    startActivity(i);
                }
                break;
            default:
                System.out.println("Cannot make chat");
                break;
        }
    }

    public void createChat() {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        if(isDoctor){

            HashMap<String, Object> hashMap=new HashMap<>();
            hashMap.put("doctorId", userId);
            hashMap.put("doctorName", userName);
            hashMap.put("patientId", patientIds.get(index));
            hashMap.put("patientName", patientNames.get(index));
            hashMap.put("status", true);
            String chatId = reference.child("Chat").push().getKey();
            hashMap.put("chatId", chatId);
            reference.child("Chat").child(chatId).setValue(hashMap);
        }else{
            HashMap<String, Object> hashMap=new HashMap<>();
            hashMap.put("doctorId", doctorIds.get(index));
            hashMap.put("doctorName", doctorNames.get(index));
            hashMap.put("patientId", userId);
            hashMap.put("patientName", userName);
            hashMap.put("status", true);
            String chatId = reference.child("Chat").push().getKey();
            hashMap.put("chatId", chatId);
            reference.child("Chat").child(chatId).setValue(hashMap);
        }
    }
}