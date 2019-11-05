package com.example.telemedicine.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telemedicine.R;
import com.example.telemedicine.models.Appointment;
import com.example.telemedicine.models.User;
import com.example.telemedicine.models.Doctor;
import com.example.telemedicine.ui.appointments.AppointmentDetails;
import com.example.telemedicine.ui.utilities.RecyclerItemOld;
import com.example.telemedicine.ui.utilities.RecyclerItemClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    DatabaseReference mDatabaseUsers;
    DatabaseReference mDatabaseDoctors;
    DatabaseReference mDatabaseAppts;

    String userid;
    String username = "";
    String appt_id = "";

    TextView welcome;
    TextView appointment_date;

    String upcoming_appt;

    View root;

    int dif_upcom_appt = 1000000;
    int idx_upcom_appt = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.home_welcome);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        welcome = (TextView)root.findViewById(R.id.home_welcome);
        appointment_date = (TextView)root.findViewById(R.id.appt_nextAppt);

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference("Users");
        mDatabaseDoctors = FirebaseDatabase.getInstance().getReference("Doctor");
        mDatabaseAppts = FirebaseDatabase.getInstance().getReference("Appointments");
        // TODO: App crashes if no user logged-in
        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    User u = child.getValue(User.class);
                    try {
                        if (u.getUserID().equalsIgnoreCase(userid)) {
                            username = u.getFirstName();
                            updateName(username);
                        }
                    }catch(NullPointerException e){
                        System.out.println("Not a user. Is a doctor");
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabaseDoctors.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    try {
                        Doctor d = child.getValue(Doctor.class);
                        if (d.getDocID().equalsIgnoreCase(userid)) {
                            username = "Dr. " + d.getFirstName();
                            updateName(username);
                        }
                    }catch(DatabaseException e){
                        System.out.println("Uh oh");
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabaseAppts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Calendar c = Calendar.getInstance((TimeZone.getTimeZone("GMT-4")), Locale.US);
                String todayid = String.format("%04d%02d%02d%02d%02d", c.get(Calendar.YEAR), (c.get(Calendar.MONTH)+1), c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Appointment a = child.getValue(Appointment.class);
                    // TODO: App crashes if no user logged-in
                    if (a.getUserID().equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        System.out.println("APPOINTMENT APPOINTMENT " + a.getApptID() + " TODAY TODAY " + todayid);
                        appt_id = a.getApptID();
                        float today_id = Float.parseFloat(todayid);
                        float apptid = Float.parseFloat(appt_id);
                        System.out.println("DIFFERENCE " + (apptid - today_id));
                        if ((apptid - today_id) > 0 && (apptid - today_id) < dif_upcom_appt) {
                            System.out.println("UPDATING UPCOMING APPOINTMENT");
                            upcoming_appt = a.getApptID();

                        }
                    }
                    else{
                        upcoming_appt = "No Upcoming Appointments";
                        appt_id = "";
                    }
                }
                // TODO: StringIndexOutOfBoundsException: length=0; index=4
                updateApptDate(appt_id);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        appointment_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AppointmentDetails.class);
                intent.putExtra("EXTRA_SESSION_ID", upcoming_appt);
                startActivity(intent);
            }
        });

        return root;
    }

    public void updateName(String name){
        welcome.setText("Welcome back, " + name);
    }

    public void updateApptDate(String date) {
        if (date.length() > 0) {
            int year = Integer.parseInt(date.substring(0, 4));
            int month = Integer.parseInt(date.substring(4, 6));
            int day = Integer.parseInt(date.substring(6, 8));
            int hour = Integer.parseInt(date.substring(8, 10));
            int min = Integer.parseInt(date.substring(10));
            String ampm = "AM";
            if (hour > 12) {
                hour -= 12;
                ampm = "PM";
            }
            String s_date = String.format("%02d/%02d/%04d at %d:%02d %s", month, day, year, hour, min, ampm);
            appointment_date.setText(s_date);
        } else {
            appointment_date.setText("No upcoming appointment");
        }
    }

}