package com.example.telemedicine.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.telemedicine.MainActivity;
import com.example.telemedicine.R;
import com.example.telemedicine.models.User;
import com.example.telemedicine.ui.signup.Signup;
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
public class Login extends AppCompatActivity {

    // Global Variables
    // User user; // Mock-Up
    EditText emailET, passwordET;
    // EditText lastFourSSNET;
    Button loginBtn, signupBtn;
    String TAG = "Login.java";
    // Firebase
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    FirebaseUser fUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Get references from the View
        emailET = (EditText) findViewById(R.id.emailET);
        passwordET = (EditText) findViewById(R.id.passwordET);
        // lastFourSSNET = (EditText) findViewById(R.id.lastFourOfSSN);
        loginBtn = (Button) findViewById(R.id.submitBtn);
        signupBtn = (Button)findViewById(R.id.signupBTN);
        // Initialize Firebase instance
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        fUser = FirebaseAuth.getInstance().getCurrentUser(); // TODO
//    }

    /**
     * Handles the clicks from 'activity_login.xml'
     * @param view - The current view
     */
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.submitBtn:
                this.checkLogin();
                break;
            case R.id.signupBtn:
                intent = new Intent(Login.this, Signup.class);
                startActivity(intent);
                finish();
                break;
            default:
                System.out.println("Something went wrong");
                break;
        }
    }

    /**
     * Called to check with firebase auth if the user is valid
     */
    private void checkLogin() {
        // Get our data from ET fields
        final String email = emailET.getText().toString().trim();
        final String password = passwordET.getText().toString().trim();
        // Check for empty parameters
        if (email.isEmpty() || password.isEmpty()) return;
        // Sign in with firebase using firebase auth instance
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If user was found
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            onAuthSuccess(task.getResult().getUser()); // TODO - Catch NullPointer
                        } else {
                            // If sign-in fails
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Auth failed..", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * If the user is auth successfully this method is called to start the next activity
     * @param user - The current signed in user after auth
     */
    protected void onAuthSuccess(FirebaseUser user) {
        // Assign global var to current logged in user
        fUser = user;
        // Start new activity
        Toast.makeText(this, "Successful Login.", Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
