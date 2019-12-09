package com.example.telemedicine.ui.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.telemedicine.R;
import com.example.telemedicine.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class AccountView extends AppCompatActivity {

    Button button;
    TextView name, email, address, phone, ssn;
    RadioGroup sex;
    RadioButton rb1, rb2, rb3;

    User user;
    String userid;

    DatabaseReference mDatabaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_view);

        button = (Button)findViewById(R.id.account_button);
        name = (TextView)findViewById(R.id.account_name_container);
        email = (TextView)findViewById(R.id.account_email_container);
        address = (TextView)findViewById(R.id.account_address_container);
        phone = (TextView)findViewById(R.id.account_phone_container);
        sex = (RadioGroup)findViewById(R.id.account_sex_container);
        ssn =  (TextView)findViewById(R.id.account_ssn_container);
        rb1 = (RadioButton)findViewById(R.id.radioButton);
        rb2 = (RadioButton)findViewById(R.id.radioButton2);
        rb3 = (RadioButton)findViewById(R.id.radioButton3);

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference("Users");

        mDatabaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    User u = child.getValue(User.class);
                    try {
                        if (u.getUserID().equalsIgnoreCase(userid)) {
                            user = u;
                            updateData();
                            break;
                        }
                    }catch(NullPointerException e){
                        System.out.println("User ID is null");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AccountEdit.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void updateData(){
        name.setText(user.getFullName());
        email.setText(user.getEmail());
        address.setText(user.getAddress());
        phone.setText(phoneNum(user.getPhoneNum()));
        setSex(user.getGender());
        ssn.setText("***-**-" + user.getLastFourSSN());
    }

    public String phoneNum(String p){
        String ph = "(" + p.substring(0, 3) + ")" + p.substring(3, 6) + "-" + p.substring(6);
        return ph;
    }

    public void setSex(String g){
        switch(g){
            case "Male":
                rb1.setChecked(true);
                rb2.setChecked(false);
                rb3.setChecked(false);
                break;
            case "Female":
                rb1.setChecked(false);
                rb2.setChecked(true);
                rb3.setChecked(false);
                break;
            default:
                rb1.setChecked(false);
                rb2.setChecked(false);
                rb3.setChecked(true);
                break;
        }
    }
}
