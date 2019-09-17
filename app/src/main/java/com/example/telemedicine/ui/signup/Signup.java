package com.example.telemedicine.ui.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.telemedicine.R;
import com.example.telemedicine.ui.login.Login;

public class Signup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.alreadyTV:
                Intent intent = new Intent(Signup.this, Login.class);
                startActivity(intent);
        }
    }
}
