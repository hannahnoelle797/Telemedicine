package com.example.telemedicine.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.telemedicine.R;
import com.example.telemedicine.models.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Login extends AppCompatActivity {

    // Global Variables
    User user; // Mock-Up
    EditText emailET, passwordET, lastFourSSNET;
    TextView forgotPassTV;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Get references from the View
        emailET = (EditText) findViewById(R.id.usernameET);
        passwordET = (EditText) findViewById(R.id.passwordET);
        lastFourSSNET = (EditText) findViewById(R.id.lastFourOfSSN);
        loginBtn = (Button) findViewById(R.id.submitBtn);
        forgotPassTV = (TextView) findViewById(R.id.forgotPassTV);
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

    // Handle all clicks within login
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.submitBtn:
                System.out.println("Submitting something");
                break;
            case R.id.forgotPassTV:
                System.out.println("Forgot password");
                break;
            default:
                System.out.println("Something went wrong");
                break;
        }
    }
}
