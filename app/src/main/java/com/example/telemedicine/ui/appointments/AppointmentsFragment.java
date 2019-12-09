package com.example.telemedicine.ui.appointments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telemedicine.MainActivity;
import com.example.telemedicine.R;
import com.example.telemedicine.models.Appointment;
import com.example.telemedicine.ui.scheduling.ApptSchedulingFirebase;
import com.example.telemedicine.ui.utilities.RecyclerItem;
import com.example.telemedicine.ui.utilities.RecyclerItemClickListener;
import com.example.telemedicine.ui.utilities.RecyclerItemOld;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class AppointmentsFragment extends Fragment implements RecyclerItem.OnReportClickListener {

    private AppointmentsViewModel appointmentsViewModel;

    private RecyclerView rvUpcoming;

    private RecyclerView.Adapter adapUpcoming;

    private RecyclerView.LayoutManager lmUpcoming;

    private DatabaseReference mDatabaseAppts;
    private DatabaseReference mDatabaseUsers;

    private ArrayList<String> upcomingAppt;
    private ArrayList<String> upcomingApptIDs;
    private ArrayList<String[]> appointments = new ArrayList<>();

    String[] arr = new String[2];

    private Calendar n;

    private View root;

    Appointment a;
    String upcoming_appt, date, appt_id;

    TextView nextAppt;
    String nextApptID;

    private String[] apptUpcoming = {"Physical - 9/29 @ 10:00am"};

    private Button btn;

    float dif_upcom_appt = 1100000000;
    int idx_upcom_appt = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        appointmentsViewModel =
                ViewModelProviders.of(this).get(AppointmentsViewModel.class);
        root = inflater.inflate(R.layout.fragment_appointments, container, false);
        final TextView textView = root.findViewById(R.id.appointment_head);
        appointmentsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        mDatabaseAppts = FirebaseDatabase.getInstance().getReference("Appointments");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference("User");

        nextAppt = (TextView)root.findViewById(R.id.upcoming_appt);

        nextAppt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AppointmentDetails.class);
                intent.putExtra("EXTRA_SESSION_ID", nextApptID);
                startActivity(intent);
            }
        });

        upcomingAppt = new ArrayList<>();

        upcomingApptIDs = new ArrayList<>();

        mDatabaseAppts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Calendar c = Calendar.getInstance((TimeZone.getTimeZone("GMT-4")), Locale.US);
                String todayid = String.format("%04d%02d%02d%02d%02d", c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    a = child.getValue(Appointment.class);
                    if(userid.equals(a.getUserID()))
                    {
                        Date today = c.getTime();

                        n = Calendar.getInstance((TimeZone.getTimeZone("GMT-4")), Locale.US);
                        n.set(Calendar.YEAR, a.getApptYear());
                        n.set(Calendar.MONTH, a.getApptMonth()-1);
                        n.set(Calendar.DAY_OF_MONTH, a.getApptDay());
                        n.set(Calendar.HOUR, a.getApptHour());
                        n.set(Calendar.MINUTE, a.getApptMin());

                        Date apptDate = n.getTime();

                        String appt = a.shortString();
                        arr = new String[2];
                        arr[0] = appt;
                        arr[1] = a.getApptID();
                        appointments.add(arr);
                        upcomingApptIDs.add(a.getApptID());

                        appt_id = a.getApptID();
                        float today_id = Float.parseFloat(todayid);
                        today_id += 1000000;
                        float apptid = Float.parseFloat(appt_id);
                        if ((apptid - today_id) > 0 && (apptid - today_id) < dif_upcom_appt) {
                            dif_upcom_appt = apptid - today_id;
                            upcoming_appt = a.getApptID();
                            date = a.getDateTime();
                            nextApptID = a.getApptID();
                            updateApptDate();
                        }
                    }
                }

                Collections.sort(upcomingApptIDs);
                Collections.reverse(upcomingApptIDs);
                for(int i = 0; i < upcomingApptIDs.size(); i++)
                {
                    for(int p = 0; p < appointments.size(); p++) {
                        if (upcomingApptIDs.get(i).equalsIgnoreCase(appointments.get(p)[1])) {
                            upcomingAppt.add(appointments.get(p)[0]);
                        }
                    }
                }

                if(upcomingAppt.size() > 0)
                    apptUpcoming = upcomingAppt.toArray(new String[upcomingAppt.size()]);
                else
                    apptUpcoming[0] = "No Upcoming Appointments";

                populateRecyclers(root);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn = (Button)root.findViewById(R.id.button_new_appt);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(getContext(), ApptSchedulingFirebase.class);
                startActivity(intent);
            }
        });
        return root;
    }

    @Override
    public void OnReportClickListener(int position) {
        Intent intent = new Intent(getContext(), AppointmentDetails.class);
        String apptID = upcomingApptIDs.get(position);
        intent.putExtra("EXTRA_SESSION_ID", apptID);
        startActivity(intent);
    }

    public void populateRecyclers(View root)
    {
        rvUpcoming = (RecyclerView)root.findViewById(R.id.recycler_appt);
        lmUpcoming = new LinearLayoutManager(this.getActivity());
        rvUpcoming.setLayoutManager(lmUpcoming);
        adapUpcoming = new RecyclerItem(apptUpcoming,this);
        rvUpcoming.setAdapter(adapUpcoming);
    }

    public void updateApptDate(){
        nextAppt.setText("Next Appointment: " + date);
    }

}