package com.example.telemedicine.ui.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.telemedicine.MainActivity;
import com.example.telemedicine.R;
import com.example.telemedicine.models.Users;
import com.example.telemedicine.ui.signup.Signup;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Login extends AppCompatActivity {

    // Global Variables
    Users user; // Mock-Up
    EditText emailET, passwordET, lastFourSSNET;
    Button loginBtn, signupBtn;
    String TAG = "Login.java";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Get references from the View
        emailET = (EditText) findViewById(R.id.usernameET);
        passwordET = (EditText) findViewById(R.id.passwordET);
        lastFourSSNET = (EditText) findViewById(R.id.lastFourOfSSN);
        loginBtn = (Button) findViewById(R.id.submitBtn);
        signupBtn = (Button)findViewById(R.id.signupBTN);
    }

    // Check for valid email/password TODO
    protected boolean correctFormat(String email, String password) {
        return true;
    }

    // Check with database for correct login TODO
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

    // Hash a given string TODO
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

    // Handle all clicks within login TODO
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.submitBtn:
                intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.signupBtn:
                intent = new Intent(Login.this, Signup.class);
                startActivity(intent);
                break;
            default:
                System.out.println("Something went wrong");
                break;
        }
    }

    // Forgot Password TODO
    protected void sendEmail() {
        // Log that email has started
        Log.i("Login.java: ", "Sending Email");

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Retrieve Password");
        alert.setMessage("Message");

        // Get input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String TO = input.toString();
                String CC = "david.howard1100@gmail.com";

                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setDataAndType(Uri.parse("mailto:"), "text/plain");

                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_CC, CC);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Reset Password");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Body Message");

                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    finish();
                    Log.i(TAG, "Finished sending email...");

                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(Login.this, "There is no email client installed", Toast.LENGTH_LONG).show();
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Toast.makeText(Login.this, "Canceled Forgot Password", Toast.LENGTH_SHORT).show();
            }
        });

        alert.show();
    }
}
