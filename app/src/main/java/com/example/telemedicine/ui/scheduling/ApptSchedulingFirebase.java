package com.example.telemedicine.ui.scheduling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.telemedicine.MainActivity;
import com.example.telemedicine.R;
import com.example.telemedicine.models.Appointment;
import com.example.telemedicine.models.Doctor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;


public class ApptSchedulingFirebase extends AppCompatActivity {

    Button schedBtn;
    Button apptBtn;

    Spinner appt_spinner;
    Spinner doc_spinner;
    Spinner time_spinner;

    ArrayList<String> doctorNames;

    Calendar c;
    DatePickerDialog dpd;

    DatabaseReference mDatabaseAppts;
    DatabaseReference mDatabaseUsers;
    DatabaseReference mDatabaseDocs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appt_scheduling_firebase);

        appt_spinner = findViewById(R.id.spinner_appt_type);
        String[] appt_types = new String[]{"Select Appointment Type", "Annual Wellness Visit", "Health Screening", "New Problem Visit", "Problem Follow Up Visit", "Physical"};
        ArrayAdapter<String> appt_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, appt_types);
        appt_spinner.setAdapter(appt_adapter);

        doc_spinner = findViewById(R.id.spinner_doctor);
        //TODO: Use Doctor table to populate doctor spinner
/*
        mDatabaseDocs = FirebaseDatabase.getInstance().getReference("Doctor");
        doctorNames = new ArrayList<>();




        mDatabaseDocs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    Doctor doc = ds.getValue(Doctor.class);
                    String name = doc.getDocString();
                    doctorNames.add(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mDatabaseDocs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    Doctor doc = ds.getValue(Doctor.class);
                    String name = doc.getDocString();
                    doctorNames.add(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        //String[] docNames = GetStringArray(doctorNames);
        String[] doc_names = new String[]{"Select Doctor", "Dr. Hayden Lee", "Dr. Jane Smith", "Dr. Amanda Parker", "Dr. Michael Dean"};
        ArrayAdapter<String> doc_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, doc_names);
        doc_spinner.setAdapter(doc_adapter);

        apptBtn = (Button)findViewById(R.id.button_date);

        apptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = Calendar.getInstance((TimeZone.getTimeZone("GMT-4")), Locale.US);

                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                dpd = new DatePickerDialog(ApptSchedulingFirebase.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        apptBtn.setText((mMonth+1) + "/" + mDay + "/" + mYear);
                    }
                }, year, month, day);
                dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dpd.show();
            }
        });

        mDatabaseAppts = FirebaseDatabase.getInstance().getReference();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference("User");

        time_spinner = findViewById(R.id.spinner_time);
        String[] avail_times = new String[]{"Select Time", "7:00 AM", "7:30 AM", "8:00 AM", "8:30 AM", "9:00 AM", "9:30 AM", "10:00 AM", "10:30 AM", "11:00 AM", "11:30 AM", "12:00 PM", "12:30 PM", "1:00 PM", "1:30 PM", "2:00 PM", "2:30 PM", "3:00 PM", "3:30 PM", "4:00 PM", "4:30 PM"};
        ArrayAdapter<String> time_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, avail_times);
        time_spinner.setAdapter(time_adapter);

        schedBtn = (Button)findViewById(R.id.button_appt_submit);
    }

    public void apptClick(View view)
    {
        switch(view.getId())
        {
            case R.id.button_appt_submit:
                if(appt_spinner.getSelectedItem().equals("Select Appointment Type"))
                {
                    Toast.makeText(ApptSchedulingFirebase.this, "Please Select Appointment Type", Toast.LENGTH_SHORT).show();
                }
                else if(doc_spinner.getSelectedItem().equals("Select Doctor"))
                {
                    Toast.makeText(ApptSchedulingFirebase.this, "Please Select Doctor", Toast.LENGTH_SHORT).show();
                }
                else if(apptBtn.getText().equals("Select Date"))
                {
                    Toast.makeText(ApptSchedulingFirebase.this, "Please Select Date", Toast.LENGTH_SHORT).show();
                }
                else if(time_spinner.getSelectedItem().equals("Select Time"))
                {
                    Toast.makeText(ApptSchedulingFirebase.this, "Please Select Time", Toast.LENGTH_SHORT).show();
                }
                else {
                    createAppointment();
                    Toast.makeText(ApptSchedulingFirebase.this, "Appointment Created", Toast.LENGTH_SHORT).show();
                    Intent intent;
                    intent = new Intent(ApptSchedulingFirebase.this, MainActivity.class);
                    startActivity(intent);
                }
                break;
            default:
                System.out.println("Cannot make appoint");
                break;
        }
    }

    public void createAppointment()
    {
        //SAMPLE APPOINTMENT TO BE REMOVED
        String id = UUID.randomUUID().toString();
        System.out.println("USER ID HERE!!!!!!!!!!!!!: " + id);
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Appointment test_appt = new Appointment(id, userid, "fjaffdk", 2019, 10, 23, 11, 0, "AM");
        mDatabaseAppts.child("Appointments").child(id).setValue(test_appt);
    }

    public String[] GetStringArray(ArrayList<String> arr)
    {

        // declaration and initialise String Array
        String str[] = new String[arr.size()];

        // ArrayList to Array Conversion
        for (int j = 0; j < arr.size(); j++) {

            // Assign each value to String array
            str[j] = arr.get(j);
        }

        return str;
    }
}
