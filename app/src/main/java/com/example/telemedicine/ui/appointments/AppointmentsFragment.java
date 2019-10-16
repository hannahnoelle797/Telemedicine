package com.example.telemedicine.ui.appointments;

import android.content.Intent;
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
import com.example.telemedicine.ui.scheduling.appointmentScheduling;
import com.example.telemedicine.ui.utilities.RecyclerItem;
import com.example.telemedicine.ui.utilities.RecyclerItemOld;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class AppointmentsFragment extends Fragment implements RecyclerItem.OnReportClickListener {

    private AppointmentsViewModel appointmentsViewModel;

    private RecyclerView rvUpcoming;
    private RecyclerView rvPrevious;

    private RecyclerView.Adapter adapUpcoming;
    private RecyclerView.Adapter adapPrevious;

    private RecyclerView.LayoutManager lmUpcoming;
    private RecyclerView.LayoutManager lmPrevious;

    private DatabaseReference mDatabaseAppts;
    private DatabaseReference mDatabaseUsers;

    private ArrayList<String> upcomingAppt;
    private ArrayList<String> previousAppt;

    private Calendar n;

    private View root;

    private String[] apptUpcoming = {"Physical - 9/29 @ 10:00am"};
    private String[] apptPrevious = {"Wellness Check - 5/19 @ 10:30am"};

    private Button btn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        appointmentsViewModel =
                ViewModelProviders.of(this).get(AppointmentsViewModel.class);
        root = inflater.inflate(R.layout.fragment_appointments, container, false);
        final TextView textView = root.findViewById(R.id.appt_upcoming_header);
        appointmentsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        mDatabaseAppts = FirebaseDatabase.getInstance().getReference("Appointments");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference("User");

        //final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //System.out.println("\n\n\nUSER ID HERE: " + userid + "\n\n\n");

        upcomingAppt = new ArrayList<>();
        previousAppt = new ArrayList<>();


        mDatabaseAppts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Appointment a = child.getValue(Appointment.class);
                    if(userid.equals(a.getUserID()))
                    {
                        Calendar c = Calendar.getInstance((TimeZone.getTimeZone("GMT-4")), Locale.US);

                        Date today = c.getTime();
                        System.out.println("CURRENT TIME: " + today.toString());

                        n = Calendar.getInstance((TimeZone.getTimeZone("GMT-4")), Locale.US);
                        n.set(Calendar.YEAR, a.getApptYear());
                        n.set(Calendar.MONTH, a.getApptMonth()-1);
                        n.set(Calendar.DAY_OF_MONTH, a.getApptDay());
                        n.set(Calendar.HOUR, a.getApptHour());
                        n.set(Calendar.MINUTE, a.getApptMin());

                        Date apptDate = n.getTime();
                        System.out.println("APPOINTMENT TIME" + apptDate.toString());

                        if(apptDate.before(today) && previousAppt.size() <= 4) {
                            String appt = a.shortString();
                            previousAppt.add(appt);
                        }
                        else if(apptDate.after(today) && upcomingAppt.size() <= 4) {
                            String appt = a.shortString();
                            upcomingAppt.add(appt);
                        }
                        else {
                            System.out.println("Uh oh");
                        }
                    }
                }
                //TODO: Populate Recylcer View methods here
                if(upcomingAppt.size() > 0)
                    apptUpcoming = upcomingAppt.toArray(new String[upcomingAppt.size()]);
                else
                    apptUpcoming[0] = "No Upcoming Appointments";
                if(previousAppt.size() > 0)
                    apptPrevious = previousAppt.toArray(new String[previousAppt.size()]);
                else
                    apptPrevious[0] = "No Previous Appointments";
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
        Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
    }

    public void populateRecyclers(View root)
    {
        rvUpcoming = (RecyclerView)root.findViewById(R.id.recycler_appt_upcoming);
        rvPrevious = (RecyclerView)root.findViewById(R.id.recycler_appt_prev);
        lmUpcoming = new LinearLayoutManager(this.getActivity());
        lmPrevious = new LinearLayoutManager(this.getActivity());
        rvUpcoming.setLayoutManager(lmUpcoming);
        rvPrevious.setLayoutManager(lmPrevious);
        adapUpcoming = new RecyclerItem(apptUpcoming, this);
        adapPrevious = new RecyclerItem(apptPrevious, this);
        rvUpcoming.setAdapter(adapUpcoming);
        rvPrevious.setAdapter(adapPrevious);
    }
}