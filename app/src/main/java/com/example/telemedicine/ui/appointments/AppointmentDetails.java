package com.example.telemedicine.ui.appointments;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.telemedicine.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AppointmentDetails extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        DatabaseReference mDatabaseAppts = FirebaseDatabase.getInstance().getReference("Appointments");

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng hospital = new LatLng(33.968761, -84.550972);
        googleMap.addMarker(new MarkerOptions().position(hospital).title("Hospital Marker"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hospital, 15));
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }
}