package com.example.telemedicine.ui.account;

import androidx.appcompat.app.AppCompatActivity;
import com.example.telemedicine.R;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class AccountView extends AppCompatActivity {

    Button button;
    TextView name, email, address, phone, ssn;
    RadioGroup sex;

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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AccountEdit.class);
                startActivity(intent);
            }
        });
    }
}
