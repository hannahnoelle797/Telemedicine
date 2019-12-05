package com.example.telemedicine.ui.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.telemedicine.R;
import com.example.telemedicine.models.User;
import com.example.telemedicine.ui.appointments.AppointmentsFragment;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class AccountEdit extends AppCompatActivity {

    Button button;
    EditText name, email, address, phone, ssn;
    TextView sex;

    Intent intent;

    DatabaseReference mDatabaseUsers, currentUser;
    String userid;
    User user, newUser;
    DataSnapshot userOld;

    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+"[a-zA-Z0-9_+&*-]+)*@"+"(?:[a-zA-Z0-9-]+\\.)+[a-z"+"A-Z]{2,7}$";
    Pattern pat = Pattern.compile(emailRegex);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_edit);

        button = (Button)findViewById(R.id.account_button);

        name = (EditText)findViewById(R.id.account_name_container);
        email = (EditText)findViewById(R.id.account_email_container);
        address = (EditText)findViewById(R.id.account_address_container);
        phone = (EditText)findViewById(R.id.account_phone_container);
        ssn = (EditText)findViewById(R.id.account_ssn_container);

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference("Users");

        mDatabaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                currentUser = FirebaseDatabase.getInstance().getReference().child(userid);

                for(DataSnapshot child : dataSnapshot.getChildren()){
                    User u = child.getValue(User.class);
                    try {
                        if (u.getUserID().equalsIgnoreCase(userid)) {
                            user = u;
                            userOld = child;
                            updateData();
                            break;
                        }
                    }catch(NullPointerException e){

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(view.getContext(), AccountView.class);
                sendToDatabase();
            }
        });*/

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(sendToDatabase()) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    Toast.makeText(view.getContext(), "Data Saved.", Toast.LENGTH_LONG).show();
                                    userOld.getRef().removeValue();
                                    User userNew = new User(user.getUserID(), name.getText().toString(), email.getText().toString(), address.getText().toString(), phone.getText().toString(), user.getGender(), Integer.parseInt(ssn.getText().toString()));
                                    mDatabaseUsers.child(user.getUserID()).setValue(userNew);
                                    Intent intent = new Intent(view.getContext(), AccountView.class);
                                    startActivity(intent);
                                    finish();
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    Toast.makeText(view.getContext(), "No Changes Made", Toast.LENGTH_LONG).show();
                                    Intent intentNo = new Intent(view.getContext(), AccountView.class);
                                    startActivity(intentNo);
                                    finish();
                                    break;
                            }
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Save Changes?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
    }

    public boolean sendToDatabase(){
        if(ssn.getText().toString().length() != 4) {
            Toast.makeText(getApplicationContext(), "Invalid Social Security Number", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!pat.matcher(email.getText().toString()).matches()){
            Toast.makeText(getApplicationContext(), "Invalid Email Address", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(phone.getText().toString().length() != 10) {
            Toast.makeText(getApplicationContext(), "Invalid Phone Number. Please do not enter special characters.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void updateData(){
        name.setText(user.getFullName());
        email.setText(user.getEmail());
        address.setText(user.getAddress());
        phone.setText(user.getPhoneNum());
        ssn.setText(Integer.toString(user.getLastFourSSN()));
    }
}
