package com.example.telemedicine.ui.utilities;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsMarkerActivity extends AppCompatActivity implements OnMapReadyCallback {
    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng hospital = new LatLng(33.968761, -84.550972);
        googleMap.addMarker(new MarkerOptions().position(hospital).title("Hospital Marker"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(hospital));
    }
}