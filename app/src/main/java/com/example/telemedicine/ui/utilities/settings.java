package com.example.telemedicine.ui.utilities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.telemedicine.MainActivity;
import com.example.telemedicine.R;
import com.example.telemedicine.ui.account.AccountView;
import com.example.telemedicine.ui.appointments.AppointmentDetails;
import com.example.telemedicine.ui.login.Login;

public class settings extends AppCompatActivity {

    TextView account, privacy, about, terms, out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        account = (TextView)findViewById(R.id.settings_account);
        privacy = (TextView)findViewById(R.id.settings_privacy);
        about = (TextView)findViewById(R.id.settings_about);
        terms = (TextView)findViewById(R.id.settings_terms);
        out = (TextView)findViewById(R.id.signout);

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AccountView.class);
                startActivity(intent);
            }
        });

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), privacy.class);
                intent.putExtra("EXTRA_SESSION_ID", "Privacy_Policy.pdf");
                startActivity(intent);
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), privacy.class);
                intent.putExtra("EXTRA_SESSION_ID", "AboutUs.pdf");
                startActivity(intent);
            }
        });

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), privacy.class);
                intent.putExtra("EXTRA_SESSION_ID", "TermsAndConditions.pdf");
                startActivity(intent);
            }
        });

        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), Login.class));
                finish();
            }
        });
    }
}