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
import com.example.telemedicine.ui.login.Login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Signup extends AppCompatActivity {

    // Global Variables
    private EditText emailET, passwordET;

    // Firebase auth instance
    private FirebaseAuth mAuth;
    // log tag
    private final String TAG = "Signup.java";
    // Global firebase user
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailET = (EditText)findViewById(R.id.emailET);
        passwordET = (EditText)findViewById(R.id.passwordET);

        // Get firebase instance
        mAuth = FirebaseAuth.getInstance();
        // Check if already sign-in TODO
    }

    // Button Click for activity_signup.xml
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.alreadyTV:
                intent = new Intent(Signup.this, Login.class);
                startActivity(intent);
                break;
            case R.id.signupBTN:

                if (emailET.getText().toString().isEmpty() || passwordET.getText().toString().isEmpty()) return;
                this.createUser(emailET.getText().toString(), passwordET.getText().toString());

                if (user == null) {
                    Log.i(TAG, "Account not created.");
                    return;
                }

                Toast.makeText(this, "Account Created.", Toast.LENGTH_LONG).show();
                intent = new Intent(Signup.this, Login.class);
                startActivity(intent);
                break;
            default:
                System.out.println("Default");
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
                            user = mAuth.getCurrentUser();
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

    // TODO Check if confirm and original match
    // i.e. password and passwordConfirm
}
