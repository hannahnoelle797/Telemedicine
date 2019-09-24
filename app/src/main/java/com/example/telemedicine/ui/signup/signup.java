package com.example.telemedicine.ui.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.telemedicine.R;
import com.example.telemedicine.ui.login.Login;

public class signup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    // Button Click for activity_signup.xml
    public void onClick(View view) {
        // Switch statement used to handle different clicks TODO
        switch (view.getId()) {
            case R.id.alreadyTV:
                Intent intent = new Intent(signup.this, Login.class);
                startActivity(intent);
        }
    }
}
