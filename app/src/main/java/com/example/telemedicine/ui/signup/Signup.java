package com.example.telemedicine.ui.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.telemedicine.MainActivity;
import com.example.telemedicine.R;
import com.example.telemedicine.models.Doctor;
import com.example.telemedicine.models.User;
import com.example.telemedicine.ui.login.Login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.ValueEventListener;

public class Signup extends AppCompatActivity {

    //TODO : Address, FullName (Instead of firstN + lastN), D.O.B, Phone#

    // Global Variables
    private EditText firstNameET, lastNameET, emailET, confirmEmailET, ssnET, confirmSSNET, passwordET, confirmPasswordET;
//    private EditText addressET, dob, phoneNum;
    private Spinner spinner;

    // Firebase auth instance
    private FirebaseAuth mAuth;
    // Firebase userDB
    //private DatabaseReference userDB, doctorDB;
    private DatabaseReference mDatabase;
    // log tag
    private final String TAG = "Signup.java";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        spinner = (Spinner) findViewById(R.id.patientOrDocSPIN);
        String [] patientOrDoctor = new String[] {"I am a patient", "I am a doctor"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, patientOrDoctor);
        spinner.setAdapter(spinnerAdapter);
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

        // Establish Database instances
//        userDB = FirebaseDatabase.getInstance().getReference("Users");
//        doctorDB = FirebaseDatabase.getInstance().getReference("Doctor"); // TODO - Change Doctor -> Doctors
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Populate spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                switch(position)
                {
                    case 0: //PATIENT
                        ssnET.setHint(getResources().getString(R.string.socialNumHint));
                        confirmSSNET.setHint(getResources().getString(R.string.socialNumConfirmHint));
                        break;
                    case 1: //EMPLOYEE
                        ssnET.setHint(getResources().getString(R.string.empIDHint));
                        confirmSSNET.setHint(getResources().getString(R.string.empIDConfirmHint));
                        break;
                    default:
                        System.out.println("Uh oh");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                ssnET.setHint(getResources().getString(R.string.socialNumHint));
                confirmSSNET.setHint(getResources().getString(R.string.socialNumConfirmHint));
            }
        });
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
                break;
            case R.id.signupBTN:
                // Check if the fields are empty
                if (emailET.getText().toString().isEmpty() || passwordET.getText().toString().isEmpty()) return;
                // Check if confirms match - Email
                if (!emailET.getText().toString().trim().equals(confirmEmailET.getText().toString().trim())){
                    Toast.makeText(Signup.this, "Your emails must match before continuing..", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Check if confirms match - Password
                if (!passwordET.getText().toString().trim().equals(confirmPasswordET.getText().toString().trim())) {
                    Toast.makeText(Signup.this, "Your passwords must match before continuing..", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Check if confirms match - SSN
                if (!ssnET.getText().toString().trim().equals(confirmSSNET.getText().toString().trim())) {
                    Toast.makeText(Signup.this, "Your SSN must match before continuing..", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Create Firebase User - using createUserWithEmailAndPassword
                createUser(emailET.getText().toString().trim(), passwordET.getText().toString().trim());
                break;
        }
    }

    private void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign up Successful
                            Log.d(TAG, "createUserWithEmail:success");
                            if (task.getResult().getUser() == null) {
                                return;
                            }
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            // If signup fails
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Signup.this, "Auth failed..", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    protected void onAuthSuccess(FirebaseUser user) {
        // TODO remove password cleartext
        // Depending on the spinner item selected, save the user/doctor data to the db
        if (spinner.getSelectedItem().equals("I am a patient")) {
            addUserLocally(user.getUid(), firstNameET.getText().toString().trim(), lastNameET.getText().toString().trim(), emailET.getText().toString().trim(),
                    passwordET.getText().toString().trim(), Integer.parseInt(ssnET.getText().toString().trim()));
        } else if (spinner.getSelectedItem().equals("I am a doctor")) {
            addDocLocally(firstNameET.getText().toString().trim(), lastNameET.getText().toString().trim(), emailET.getText().toString().trim(),
                passwordET.getText().toString().trim(), user.getUid(), getFullName(firstNameET.getText().toString().trim(), lastNameET.getText().toString().trim()), Integer.parseInt(ssnET.getText().toString().trim()));
        } else {
            System.out.println("Uh oh");
        }
        // Display text to user to let them know that the account was created successfully
        Toast.makeText(this, "Account Created.", Toast.LENGTH_LONG).show();
        // Start new Intent
        startActivity(new Intent(Signup.this, MainActivity.class));
        finish();
    }

    // Add data from the user to the database
    protected void addUserLocally(String userID, String firstName, String lastName, String email, String password, int last4SSN) {
        User user = new User(userID, firstName, lastName, email, password, last4SSN);
        if (mAuth.getCurrentUser() == null) {
            return;
        }
        mDatabase.child("Users").child(userID).setValue(user);
        // userDB.child(userID).setValue(user); // TODO
    }

    // Add data from the doctor to the database
    protected void addDocLocally(String firstName, String lastName, String email, String password, String docID, String docString, int empNum) {
        Doctor doctor = new Doctor(firstName, lastName, email, password, docID, docString, empNum);
        if (mAuth.getCurrentUser() == null) {
            return;
        }
        mDatabase.child("Doctor").child(docID).setValue(doctor);
        // doctorDB.child(mAuth.getCurrentUser().getUid()).setValue(doctor); // TODO
    }

    protected String getFullName(String s1, String s2) {
        return ("Dr. " + s1 + " " + s2);
    }
}
