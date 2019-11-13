package com.example.telemedicine.ui.account;

import androidx.appcompat.app.AppCompatActivity;
import com.example.telemedicine.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class AccountEdit extends AppCompatActivity {

    Button button;
    EditText name, email, address, phone, ssn;
    TextView sex;

    Intent intent;

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
        sex = (TextView)findViewById(R.id.account_sex_container);
        ssn = (EditText)findViewById(R.id.account_ssn_container);
        ssn.setText(ssn.getText().toString().substring(7));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(view.getContext(), AccountView.class);
                sendToDatabase();
            }
        });
    }

    public void sendToDatabase(){
        Toast.makeText(getApplicationContext(),"Writing data to database",Toast.LENGTH_SHORT).show();
        if(ssn.getText().toString().length() != 4) {
            Toast.makeText(getApplicationContext(), "Invalid Social Security Number", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(!pat.matcher(email.getText().toString()).matches()){
            Toast.makeText(getApplicationContext(), "Invalid Email Address", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(phone.getText().toString().length() != 10) {
            Toast.makeText(getApplicationContext(), "Invalid Phone Number", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(intent);
    }
}
