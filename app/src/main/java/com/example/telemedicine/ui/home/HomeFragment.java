package com.example.telemedicine.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.telemedicine.R;
import com.example.telemedicine.models.Appointment;
import com.example.telemedicine.models.User;
import com.example.telemedicine.models.Doctor;
import com.example.telemedicine.ui.appointments.AppointmentDetails;
import com.example.telemedicine.ui.appointments.AppointmentsFragment;
import com.example.telemedicine.ui.chats.ChatsFragment;
import com.example.telemedicine.ui.login.Login;
import com.example.telemedicine.ui.reports.ReportsFragment;
import com.example.telemedicine.ui.utilities.settings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.List;
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

    String upcoming_appt, date;

    View root;

    float dif_upcom_appt = 1000000;
    int idx_upcom_appt = 0;

    List<String> list;
    int[] imageId = {R.drawable.homecalendar, R.drawable.homefile, R.drawable.homesmartphone, R.drawable.homesettings};
    String[] web = {"Appointments", "Reports", "Chats", "Settings"};

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

        ImageAdapter adapter = new ImageAdapter(getContext(), web, imageId);
        GridView grid = (GridView)root.findViewById(R.id.grid_view);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getContext(), "Int: " + i, Toast.LENGTH_SHORT).show();
                switch (i){
                    case 0:
                        Fragment apptFrag = new AppointmentsFragment();
                        FragmentTransaction apptTrans = getFragmentManager().beginTransaction();
                        apptTrans.replace(R.id.nav_host_fragment, apptFrag);
                        apptTrans.addToBackStack(null);
                        apptTrans.commit();
                        break;
                    case 1:
                        Fragment reportFrag = new ReportsFragment();
                        FragmentTransaction reportTrans = getFragmentManager().beginTransaction();
                        reportTrans.replace(R.id.nav_host_fragment, reportFrag);
                        reportTrans.addToBackStack(null);
                        reportTrans.commit();
                        break;
                    case 2:
                        Fragment chatFrag = new ChatsFragment();
                        FragmentTransaction chatTrans = getFragmentManager().beginTransaction();
                        chatTrans.replace(R.id.nav_host_fragment, chatFrag);
                        chatTrans.addToBackStack(null);
                        chatTrans.commit();
                        break;
                    case 3:
                        Intent intent4 = new Intent(getContext(), settings.class);
                        startActivity(intent4);
                        break;
                }
            }
        });

        welcome = (TextView)root.findViewById(R.id.home_welcome);
        appointment_date = (TextView)root.findViewById(R.id.appt_nextAppt);

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference("Users");
        mDatabaseDoctors = FirebaseDatabase.getInstance().getReference("Doctor");
        mDatabaseAppts = FirebaseDatabase.getInstance().getReference("Appointments");
        // TODO: App crashes if no user logged-in
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser fUser = mAuth.getCurrentUser();
        if (fUser == null || mAuth.getCurrentUser() == null) {
            startActivity(new Intent(getContext(), Login.class));
            getActivity().getFragmentManager().popBackStack();
        }
        userid = mAuth.getUid();

        mDatabaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    User u = child.getValue(User.class);
                    try {
                        if (u.getUserID().equalsIgnoreCase(userid)) {
                            username = u.getFullName();
                            updateName(username);
                        }
                    }catch(NullPointerException e){
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
                            username = "Dr. " + d.getFullName();
                            updateName(username);
                        }
                    }catch(DatabaseException e){
                        System.out.println("Firebase database exception error");
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
                        appt_id = a.getApptID();
                        float today_id = Float.parseFloat(todayid);
                        float apptid = Float.parseFloat(appt_id);
                        if ((apptid - today_id) > 0 && (apptid - today_id) < dif_upcom_appt) {
                            dif_upcom_appt = apptid - today_id;
                            upcoming_appt = a.getApptID();
                            date = a.getDateTime();
                            updateApptDate();
                            break;
                        }
                    }
                    else{
                        date = "No Upcoming Appointments";
                        appt_id = "";
                        updateApptDate();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        appointment_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!appointment_date.getText().toString().equalsIgnoreCase("No upcoming appointment")) {
                    Intent intent = new Intent(getContext(), AppointmentDetails.class);
                    intent.putExtra("EXTRA_SESSION_ID", upcoming_appt);
                    startActivity(intent);
                }
            }
        });

        return root;
    }

    public void updateName(String name){
        welcome.setText("Welcome back, " + name);
    }

    public void updateApptDate() {
        //if (date.length() > 0) {
            /*int year = Integer.parseInt(date.substring(0, 4));
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
            appointment_date.setText(s_date);*/
        appointment_date.setText(date);
        //} else {
        //    appointment_date.setText("No upcoming appointment");
        //}
    }

}