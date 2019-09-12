package com.example.telemedicine.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.telemedicine.R;
import com.example.telemedicine.models.User;
import com.example.telemedicine.ui.home.HomeViewModel;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Login extends AppCompatActivity {

    // Global Variables
    User user; // Mock-Up
    EditText emailET, passwordET, lastFourSSN;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailET = (EditText)findViewById(R.id.usernameET);
        passwordET = (EditText)findViewById(R.id.passwordET);
        lastFourSSN = (EditText)findViewById(R.id.lastFourOfSSN);
        loginBtn = (Button)findViewById(R.id.submitBtn);
    }

    // Check for valid email/password
    protected boolean correctFormat(String email, String password) {
        return true;
    }

    // Check with database for correct login
    protected boolean checkLogin(String email, String password, int lastFourSSN) {
        if (correctFormat(user.getEmail(), user.getPassword())) {
            String hashedPassword = md5(user.getPassword());
            if (hashedPassword == user.getPassword()) {
                // if (userNameET == user.getUserName()){ return true; }
            }
        }
        // Nothing matched
        return false;
    }

    // Hash a given string
    protected String md5(String pass) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(pass.getBytes());
            byte messageDigest [] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++ ) {
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return ""; // Invalid
    }

    // On Login Submit click
    public void onClick(View view) {
        switch (view.getId()) {
            // If submit button is selected
            case R.id.submitBtn:
                // Get data from the edit text fields on click
                String email = emailET.toString();
                String password = md5(passwordET.toString()); // Hashed password
                int lastFour = Integer.parseInt(lastFourSSN.getText().toString()); // Should get int
                // Check if valid user
                if (checkLogin(email, password, lastFour)) {
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, HomeViewModel.class);
                    startActivity(intent); // opens new activity
                } else {
                    Toast.makeText(this, "Login not successful", Toast.LENGTH_SHORT).show();
                    // Open Login Denied
                }
                break;
                // If the forgot button is selected
            case R.id.forgotPassTV:
                System.out.println("Forgot password");
                break;
        }
    }
}
