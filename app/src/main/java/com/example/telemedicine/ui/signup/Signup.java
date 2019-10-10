package com.example.telemedicine.ui.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.telemedicine.R;
import com.example.telemedicine.models.User;
import com.example.telemedicine.ui.login.Login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {

    // Global Variables
    private EditText firstNameET, lastNameET, emailET, confirmEmailET, ssnET, confirmSSNET, passwordET, confirmPasswordET;

    // Firebase auth instance
    private FirebaseAuth mAuth;
    // log tag
    private final String TAG = "Signup.java";
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        firstNameET = (EditText) findViewById(R.id.firstNameET);
        lastNameET = (EditText) findViewById(R.id.lastNameET);
        emailET = (EditText)findViewById(R.id.emailET);
        confirmEmailET = (EditText) findViewById(R.id.emailConfirmET);
        ssnET = (EditText) findViewById(R.id.ssnET);
        confirmSSNET = (EditText) findViewById(R.id.ssnConfirmET);
        passwordET = (EditText)findViewById(R.id.passwordET);
        confirmPasswordET = (EditText) findViewById(R.id.passwordConfirmET);

        // Get firebase instance
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    // Button Click for activity_signup.xml
    public void onClick(View view) {
        // Create intent to switch between views
        Intent intent;
        // Handle all button clicks
        switch (view.getId()) {
            // If account already exists
            case R.id.alreadyTV:
                intent = new Intent(Signup.this, Login.class);
                startActivity(intent);
                finish();
                break;
            case R.id.signupBTN:
                // Check if the fields are empty
                if (emailET.getText().toString().isEmpty() || passwordET.getText().toString().isEmpty()) return;
                // Check if confirms match
                if (!checkEntries(emailET.getText().toString(), confirmEmailET.getText().toString())) {
                    Toast.makeText(Signup.this, "Your emails must match before continuing..", Toast.LENGTH_SHORT).show();
                }
                // Check if confirms match
                if (!checkEntries(passwordET.getText().toString(), confirmPasswordET.getText().toString())) {
                    Toast.makeText(Signup.this, "Your passwords must match before continuing..", Toast.LENGTH_SHORT).show();
                }
                // Create Firebase User - using createUserWithEmailandPassword
                createUser(emailET.getText().toString().trim(), passwordET.getText().toString().trim());
                break;
        }
    }

    // Hash password TODO
    private void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign up Successful
                            Log.d(TAG, "createUserWithEmail:success");
                            onAuthSuccess(task.getResult().getUser());
                            // UPDATE UI
                        } else {
                            // If signup fails
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Signup.this, "Auth failed..", Toast.LENGTH_SHORT).show();
                            // UPDATE UI
                        }
                    }
                });
    }

    protected void onAuthSuccess(FirebaseUser user) {
        // Write data to user object
        //TODO Test
        System.out.println("UserID: " + user.getUid());
        addUserLocally(user.getUid(), firstNameET.getText().toString().trim(), lastNameET.getText().toString().trim(), emailET.getText().toString().trim(),
                passwordET.getText().toString().trim(), Integer.parseInt(ssnET.getText().toString().trim()));
        // Display text to user to let them know that the account was created successfully
        Toast.makeText(this, "Account Created.", Toast.LENGTH_LONG).show();
        // Start new Intent
        startActivity(new Intent(Signup.this, Login.class));
        finish();
    }

    protected void addUserLocally(String userID, String firstName, String lastName, String email, String password, int last4SSN) {
        // TODO last 4
        User user = new User(userID, firstName, lastName, email, password, last4SSN);
        mDatabase.child("Users").child(userID).setValue(user);
    }

    protected boolean checkEntries (String s1, String s2) {
        return (s1 == s2);
    }
}
