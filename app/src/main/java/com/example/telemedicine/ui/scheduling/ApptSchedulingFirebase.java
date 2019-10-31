package com.example.telemedicine.ui.scheduling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.telemedicine.MainActivity;
import com.example.telemedicine.R;
import com.example.telemedicine.models.Appointment;
import com.example.telemedicine.models.Doctor;
import com.example.telemedicine.models.Location;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;


public class ApptSchedulingFirebase extends AppCompatActivity {

    Button schedBtn;
    Button apptBtn;

    Spinner appt_spinner;
    Spinner doc_spinner;
    Spinner time_spinner;
    Spinner loc_spinner;

    String date;
    String date_today;
    String time_today;

    boolean officeClosed = false;

    TextView loc_label;
    TextView doc_label;
    TextView date_label;
    TextView time_label;

    ArrayList<String> doctorNames;
    ArrayList<String> doctorIDs;

    ArrayList<String> locationNames;
    ArrayList<String> locationIDs;

    ArrayAdapter<String> time_adapter;

    String[] avail_times;
    String[] all_times = new String[]{"07:00 AM", "07:30 AM", "08:00 AM", "08:30 AM", "09:00 AM", "09:30 AM", "10:00 AM", "10:30 AM", "11:00 AM", "11:30 AM", "12:00 PM", "12:30 PM", "01:00 PM", "01:30 PM", "02:00 PM", "02:30 PM", "03:00 PM", "03:30 PM", "04:00 PM", "04:30 PM"};
    ArrayList<String> takenTimes;

    Calendar c;
    DatePickerDialog dpd;

    DatabaseReference mDatabaseAppts;
    DatabaseReference mDatabaseUsers;
    DatabaseReference mDatabaseDocs;
    DatabaseReference mDatabaseLocs;

    Context con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appt_scheduling_firebase);

        con = this;

        appt_spinner = findViewById(R.id.spinner_appt_type);
        String[] appt_types = new String[]{" ", "Annual Wellness Visit", "Health Screening", "New Problem Visit",
                "Problem Follow Up Visit", "Physical", "Blood Work"};
        ArrayAdapter<String> appt_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, appt_types);
        appt_spinner.setAdapter(appt_adapter);

        appt_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = appt_spinner.getSelectedItem().toString();
                if(!selected.equalsIgnoreCase(" ")){
                    populateLocation();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mDatabaseAppts = FirebaseDatabase.getInstance().getReference();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild("Appointments")) {
                    mDatabaseAppts = FirebaseDatabase.getInstance().getReference("Appointments");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference("User");
    }

    public void apptClick(View view)
    {
        switch(view.getId())
        {
            case R.id.button_appt_submit:
                if(appt_spinner.getSelectedItem().equals(" "))
                {
                    Toast.makeText(ApptSchedulingFirebase.this, "Please Select Appointment Type", Toast.LENGTH_SHORT).show();
                }
                else if(doc_spinner.getSelectedItem().equals(" "))
                {
                    Toast.makeText(ApptSchedulingFirebase.this, "Please Select Doctor", Toast.LENGTH_SHORT).show();
                }
                else if(apptBtn.getText().equals(" "))
                {
                    Toast.makeText(ApptSchedulingFirebase.this, "Please Select Date", Toast.LENGTH_SHORT).show();
                }
                else if(time_spinner.getSelectedItem().equals(" ") || time_spinner.getSelectedItem().equals("No appointments available on " + apptBtn.getText() + ".") || time_spinner.getSelectedItem().equals("Choose a new date."))
                {
                    if(time_spinner.getSelectedItem().equals("No appointments available on " + apptBtn.getText() + ".") || time_spinner.getSelectedItem().equals("Choose a new date."))
                        Toast.makeText(ApptSchedulingFirebase.this, "Please Select a New Date", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(ApptSchedulingFirebase.this, "Please Select Valid Time", Toast.LENGTH_SHORT).show();

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
                System.out.println("Cannot make appointment");
                break;
        }
    }

    public void createAppointment()
    {
        String id = UUID.randomUUID().toString();
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatePicker dp = dpd.getDatePicker();
        int idx = doc_spinner.getSelectedItemPosition();
        String docid = doctorIDs.get(idx);
        String time = time_spinner.getSelectedItem().toString();
        int locPos = loc_spinner.getSelectedItemPosition();
        String loc = locationIDs.get(locPos);
        int hour = Integer.parseInt(time.substring(0, 2));
        String ampm = time.substring(6);
        if(ampm.equalsIgnoreCase("pm"))
            hour += 12;
        int min = Integer.parseInt(time.substring(3, 5));
        String month = String.format("%02d", (dp.getMonth()+1));
        String day = String.format("%02d", dp.getDayOfMonth());
        String type = appt_spinner.getSelectedItem().toString();
        String appt_id = Integer.toString(dp.getYear()) + month + day + hour + time.substring(3, 5);
        Appointment test_appt = new Appointment(appt_id, userid, docid, dp.getYear(), (dp.getMonth()+1), dp.getDayOfMonth(), Integer.parseInt(time.substring(0, 2)), min, ampm, type, loc);
        mDatabaseAppts.child(appt_id).setValue(test_appt);
    }

    public void populateDate() {
        apptBtn = (Button)findViewById(R.id.button_date);
        date_label = (TextView)findViewById(R.id.appt_date);

        apptBtn.setVisibility(View.VISIBLE);
        date_label.setVisibility(View.VISIBLE);

        takenTimes = new ArrayList<>();


        apptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = Calendar.getInstance((TimeZone.getTimeZone("GMT-4")), Locale.US);

                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);
                String ampm_today = "AM";
                if(c.get(Calendar.AM_PM) == 1)
                    ampm_today = "PM";
                int min = c.get(Calendar.MINUTE);
                if(min < 30)
                    min = 0;
                else
                    min = 30;
                date_today = String.format("%d/%d/%d", (month+1), day, year);
                time_today = String.format("%02d:%02d %s", c.get(Calendar.HOUR), min, ampm_today);
                if(c.get(Calendar.HOUR) >= 5) {
                    officeClosed = true;
                }

                dpd = new DatePickerDialog(ApptSchedulingFirebase.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        apptBtn.setText((mMonth+1) + "/" + mDay + "/" + mYear);
                        date = apptBtn.getText().toString();
                        findAvailableTimes();
                    }
                }, year, month, day);

                dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dpd.show();
            }
        });
    }

    public void populateTime(){
        time_spinner = findViewById(R.id.spinner_time);
        time_label = (TextView)findViewById(R.id.appt_time);
        time_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, avail_times);
        time_spinner.setAdapter(time_adapter);
        time_label.setVisibility(View.VISIBLE);
        time_spinner.setVisibility(View.VISIBLE);

        time_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!time_spinner.getSelectedItem().toString().equalsIgnoreCase("")) {
                    populateSubmit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void findAvailableTimes() {
        final ArrayList<String> unavailable_times = new ArrayList<>();
        unavailable_times.clear();
        final List<String> all_avail_times = Arrays.asList(all_times);
        final ArrayList<String> times = new ArrayList<>();
        times.add(" ");
        mDatabaseAppts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                unavailable_times.clear();
                unavailable_times.add(" ");
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Appointment a = child.getValue(Appointment.class);
                    String time_appt = a.getTimeFormat();


                    if(a.getDateFormat().equalsIgnoreCase(date) && a.getDoctorID().equalsIgnoreCase(doctorIDs.get(doc_spinner.getSelectedItemPosition()))) {
                        unavailable_times.add(time_appt);
                    }
                    else if(date != null) {
                        if(date_today.equalsIgnoreCase(date)) {
                            if(officeClosed){
                                for(int k = 0; k < all_avail_times.size(); k++)
                                {
                                    unavailable_times.add(all_avail_times.get(k));
                                }
                            }
                            else {
                                int idx = all_avail_times.indexOf(time_today);
                                for (int j = 0; j <= idx; j++) {
                                    unavailable_times.add(all_avail_times.get(j));
                                }
                            }
                        }
                    }
                }
                for (String item : all_avail_times) {
                    if (!(unavailable_times.contains(item))) {
                        times.add(item);
                    }
                }
                if(times.size() <= 1) {
                    times.add("No appointments available on " + apptBtn.getText() + ".");
                    times.add("Choose a new date.");
                }
                avail_times = times.toArray(new String[0]);
                populateTime();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void populateSubmit(){
        schedBtn = (Button)findViewById(R.id.button_appt_submit);
        schedBtn.setVisibility(View.VISIBLE);
    }

    public void populateDoctor() {
        doc_spinner = findViewById(R.id.spinner_doctor);

        doc_label = (TextView)findViewById(R.id.label_doctor);

        mDatabaseDocs = FirebaseDatabase.getInstance().getReference("Doctor");
        doctorNames = new ArrayList<>();
        doctorIDs = new ArrayList<>();
        doctorNames.add(" ");
        doctorIDs.add(" ");

        mDatabaseDocs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Doctor d = child.getValue(Doctor.class);
                    doctorNames.add(d.getDocString());
                    doctorIDs.add(d.getDocID());
                }
                populateDocSpinner(doctorNames);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void populateDocSpinner(ArrayList<String> doctorNames)
    {
        String[] doc_names = doctorNames.toArray(new String[doctorNames.size()]);
        ArrayAdapter<String> doc_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, doc_names);
        doc_spinner.setAdapter(doc_adapter);
        doc_spinner.setVisibility(View.VISIBLE);
        doc_label.setVisibility(View.VISIBLE);

        doc_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = doc_spinner.getSelectedItem().toString();
                if(!selected.equalsIgnoreCase(" ")){
                    populateDate();
                    findAvailableTimes();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void populateLocation() {
        loc_spinner = findViewById(R.id.spinner_appt_loc);
        locationNames = new ArrayList<>();
        locationIDs = new ArrayList<>();

        loc_label = (TextView)findViewById(R.id.label_loc);
        locationNames.add(" ");
        locationIDs.add(" " );

        mDatabaseLocs = FirebaseDatabase.getInstance().getReference("Locations");

        mDatabaseLocs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Location l = child.getValue(Location.class);
                    locationNames.add(l.getName());
                    locationIDs.add(l.getId());
                }
                populateLocSpinner(locationNames);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void populateLocSpinner(ArrayList<String> locationNames)
    {
        String[] loc_names = locationNames.toArray(new String[locationNames.size()]);
        ArrayAdapter<String> loc_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, loc_names);
        loc_spinner.setAdapter(loc_adapter);
        loc_label.setVisibility(View.VISIBLE);
        loc_spinner.setVisibility(View.VISIBLE);

        loc_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = loc_spinner.getSelectedItem().toString();
                if(!selected.equalsIgnoreCase(" ")){
                    populateDoctor();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
