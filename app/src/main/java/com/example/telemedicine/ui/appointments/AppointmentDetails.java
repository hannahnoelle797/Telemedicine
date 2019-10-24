package com.example.telemedicine.ui.appointments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.telemedicine.R;
import com.example.telemedicine.models.Appointment;
import com.example.telemedicine.models.Doctor;
import com.example.telemedicine.models.Location;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AppointmentDetails extends AppCompatActivity {

    String apptID, docName, locName = "Hopsital", locAddress = "123 Middle of the Atlantic";
    String locLat = "0", locLong ="0";

    Appointment appt;
    Location loc;
    Doctor doc;

    DatabaseReference mDatabaseAppts, mDatabaseLocs, mDatabaseDocs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);
        apptID = getIntent().getStringExtra("EXTRA_SESSION_ID");
        Toast.makeText(this, apptID, Toast.LENGTH_LONG).show();

        mDatabaseAppts = FirebaseDatabase.getInstance().getReference("Appointments");
        mDatabaseLocs = FirebaseDatabase.getInstance().getReference("Locations");
        mDatabaseDocs = FirebaseDatabase.getInstance().getReference("Doctor");

        mDatabaseAppts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Appointment a = child.getValue(Appointment.class);
                    if(a.getApptID().equalsIgnoreCase(apptID)){
                        appt = new Appointment(a.getApptID(), a.getUserID(), a.getDoctorID(), a.getApptYear(), a.getApptMonth(), a.getApptDay(), a.getApptHour(), a.getApptMin(), a.getAmpm(), a.getType(), a.getLocationID());
                        getData();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
/*
    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng hospital = new LatLng(locLat, locLong);
        googleMap.addMarker(new MarkerOptions().position(hospital).title(locName + "\n" + locAddress));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hospital, 15));
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }*/

    public void getData(){
        mDatabaseDocs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Doctor a = child.getValue(Doctor.class);
                    if(a.getDocID().equalsIgnoreCase(appt.getDoctorID())) {
                        docName = a.getDocString();
                        doc = new Doctor();
                        doc.setDocString(docName);
                        setDocName();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabaseLocs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Location a = child.getValue(Location.class);
                    if(a.getId().equalsIgnoreCase(appt.getLocationID())) {
                        locLat = a.getLatitude();
                        locLong = a.getLongitude();
                        locName = a.getName();
                        locAddress = a.getAddress();
                        loc = new Location();
                        loc.setAddress(locAddress);
                        loc.setName(locName);
                        loc.setLatitude(locLat);
                        loc.setLongitude(locLong);
                        loc.setLatlong(a.getLatlong());
                        setLoc();
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        TextView apptType = (TextView)findViewById(R.id.apptType);
        apptType.setText(appt.getType());

        TextView datetime = (TextView)findViewById(R.id.datetime);
        datetime.setText(appt.getDateTime());
    }

    public void setDocName(){
        TextView doctorName = (TextView)findViewById(R.id.doc_name);
        doctorName.setText(doc.getDocString());
    }

    public void setLoc() {
        TextView locAd = (TextView)findViewById(R.id.apptAddress);
        locAd.setText(loc.getAddress());
        setMapFragment();
    }

    public void setMapFragment(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                String[] coor = loc.getLatlong().split(",");
                double latitude = Double.parseDouble(coor[0]);
                double longitude = Double.parseDouble(coor[1]);
                System.out.println("LATITUDE AND LONGITUDE " + coor[0] + " " + coor[1] + " LATITUDE AND LONGITUDE");
                LatLng hospital = new LatLng(latitude, longitude);
                googleMap.addMarker(new MarkerOptions().position(hospital).title(loc.getName()));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hospital, 15));
                googleMap.animateCamera(CameraUpdateFactory.zoomIn());
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);
            }
        });
    }
}
