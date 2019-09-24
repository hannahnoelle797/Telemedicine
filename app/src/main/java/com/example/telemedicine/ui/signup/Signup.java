package com.example.telemedicine.ui.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.telemedicine.R;
import com.example.telemedicine.ui.login.Login;

public class Signup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    // Button Click for activity_signup.xml
    public void onClick(View view) throws InterruptedException {
        // Switch statement used to handle different clicks TODO
        Intent intent;
        switch (view.getId()) {
            case R.id.alreadyTV:
                intent = new Intent(Signup.this, Login.class);
                startActivity(intent);
                break;
            case R.id.signupBTN:
                Toast.makeText(this, "Account Created.", Toast.LENGTH_LONG).show();
                intent = new Intent(Signup.this, Login.class);
                startActivity(intent);
                break;
            default:
                System.out.println("Default");
                break;
        }
    }
}
