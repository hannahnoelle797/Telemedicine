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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * @author - David Howard
 */
public class Signup extends AppCompatActivity {

    //TODO : Address, FullName (Instead of firstN + lastN), D.O.B, Phone#

    // Global Variables
    private EditText fullNameET, emailET, confirmEmailET, ssnET, confirmSSNET, phoneNumET, streetET, passwordET, confirmPasswordET;
    private Spinner genderSpinner;
    private Spinner spinner;

    // Firebase auth instance
    private FirebaseAuth mAuth;
    // Firebase userDB
    protected DatabaseReference mDatabase;
    // log tag
    private final String TAG = "Signup.java";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Get references and populate spinners
        spinner = (Spinner) findViewById(R.id.patientOrDocSPIN);
        String [] patientOrDoctor = new String[] {"I am a patient", "I am a doctor"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, patientOrDoctor);
        spinner.setAdapter(spinnerAdapter);
        fullNameET = (EditText)findViewById(R.id.fullnameET);
        genderSpinner = (Spinner)findViewById(R.id.genderSpin);
        String [] genderOptions = new String[] {"Male", "Female", "Other"};
        ArrayAdapter<String> genderSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genderOptions);
        genderSpinner.setAdapter(genderSpinnerAdapter);
        emailET = (EditText)findViewById(R.id.emailET);
        confirmEmailET = (EditText) findViewById(R.id.emailConfirmET);
        phoneNumET = (EditText)findViewById(R.id.phoneNumET);
        streetET = (EditText)findViewById(R.id.streetAddressET);
        ssnET = (EditText) findViewById(R.id.ssnET);
        confirmSSNET = (EditText) findViewById(R.id.ssnConfirmET);
        passwordET = (EditText)findViewById(R.id.passwordET);
        confirmPasswordET = (EditText) findViewById(R.id.passwordConfirmET);

        // Get firebase instance
        mAuth = FirebaseAuth.getInstance();

        // Establish Database instance
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

            /**
             * If nothing is selected in the spinner we call this
             * @param parentView - The TopLevel View (parent)
             */
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                ssnET.setHint(getResources().getString(R.string.socialNumHint));
                confirmSSNET.setHint(getResources().getString(R.string.socialNumConfirmHint));
            }
        });
    }

    /**
     * Handles the button clicks from 'activity_signup.xml'
     * @param view - The current view
     */
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
            // TODO - fix multiple toasts from stacking
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
                // Create Firebase User - using createUserWithEmailandPassword
                createUser(emailET.getText().toString().trim(), passwordET.getText().toString().trim());
                break;
        }
    }

    /**
     * Called when we need to create a user with firebase auth
     * @param email - The selected email from the edittext field
     * @param password - The selected password from the edittext field
     */
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

    /**
     * Called when the firebase auth was successful and passes our current fUser
     * @param user - The current signed-in user after creating
     */
    protected void onAuthSuccess(FirebaseUser user) {
        // Write data to user object
        if (spinner.getSelectedItem().equals("I am a patient")) {
            addUserLocally(user.getUid(), fullNameET.getText().toString().trim(), emailET.getText().toString().trim(),
                    streetET.getText().toString().trim(), phoneNumET.getText().toString().trim(), genderSpinner.getSelectedItem().toString(),
                    Integer.parseInt((getLastFour(ssnET.getText().toString().trim()))));
        } else if (spinner.getSelectedItem().equals("I am a doctor")) {
            addDocLocally(user.getUid(), fullNameET.getText().toString().trim(), emailET.getText().toString().trim(),
                    streetET.getText().toString().trim(), phoneNumET.getText().toString().trim(), genderSpinner.getSelectedItem().toString().trim(), "Dr. ".concat(fullNameET.getText().toString().trim()),
                    Integer.parseInt(ssnET.getText().toString().trim()));
        } else {
            System.out.println("Uh oh");
        }
        // Display text to user to let them know that the account was created successfully
        Toast.makeText(this, "Account Created.", Toast.LENGTH_LONG).show();
        // Start new Intent
        startActivity(new Intent(Signup.this, MainActivity.class));
        finish();
    }

    /**
     * Called when the user is auth'd, add User to db table
     * @param userId - The Firebase given userId
     * @param fullName - The firstName + lastName
     * @param email - The current Email
     * @param streetAddress - The current street address
     * @param phoneNum - The current phoneNumber
     * @param gender - The current gender
     * @param last4SSN - Last 4 of SSN
     */
    protected void addUserLocally(String userId, String fullName, String email, String streetAddress, String phoneNum, String gender, int last4SSN) {
        User user = new User(userId, fullName, email, streetAddress, phoneNum, gender, last4SSN);
        if (mAuth.getCurrentUser() == null) {
            return;
        }
        mDatabase.child("Users").child(userId).setValue(user);
    }

    /**
     * Called when the doctor is auth'd, add Doctor to the db table
     * @param docId - Firebase given userid
     * @param fullName - firstName + lastName
     * @param email - The current Email
     * @param streetAddress - The current street address
     * @param phoneNum - The current phoneNumber
     * @param gender - The current gender
     * @param docString - 'Dr' + fullName
     * @param empNum - The work given id of doc
     */
    protected void addDocLocally(String docId, String fullName, String email, String streetAddress, String phoneNum, String gender, String docString, int empNum) {
        Doctor doctor = new Doctor(docId, fullName, email, streetAddress, phoneNum, gender, docString, empNum);
        if (mAuth.getCurrentUser() == null) {
            return;
        }
        mDatabase.child("Doctor").child(docId).setValue(doctor);
    }

    /**
     * Get the last 4 char of a string
     * @param numSeq - String given
     * @return - Returns the last 4 char of string
     */
    protected String getLastFour(String numSeq) {
        if (numSeq.length() > 4) {
            return (numSeq.substring(numSeq.length() - 4));
        } else {
            // Less than 4 length
            return numSeq;
        }
    }
}
