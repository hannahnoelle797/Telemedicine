package com.example.telemedicine.ui.scheduling;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.telemedicine.MainActivity;
import com.example.telemedicine.R;

import java.util.Calendar;

public class appointmentScheduling extends AppCompatActivity {

    Button apptBtn;
    Button timeBtn;
    Button schedBtn;

    Calendar c;
    DatePickerDialog dpd;

    TimePickerDialog tpd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_scheduling);

        Spinner appt_spinner = findViewById(R.id.spinner_appt_type);
        String[] appt_types = new String[]{"Select Appointment Type", "Annual Wellness Visit", "Health Screening", "New Problem Visit", "Problem Follow Up Visit", "Physical"};
        ArrayAdapter<String> appt_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, appt_types);
        appt_spinner.setAdapter(appt_adapter);

        Spinner doc_spinner = findViewById(R.id.spinner_doctor);
        String[] doc_names = new String[]{"Select Doctor", "Dr. Hayden Lee", "Dr. Jane Smith", "Dr. Amanda Parker", "Dr. Michael Dean"};
        ArrayAdapter<String> doc_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, doc_names);
        doc_spinner.setAdapter(doc_adapter);

        apptBtn = (Button)findViewById(R.id.button_date);

        apptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                dpd = new DatePickerDialog(appointmentScheduling.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        apptBtn.setText(mDay + "/" + (mMonth+1) + "/" + mYear);
                    }
                }, day, month, year);
                dpd.show();
            }
        });

        timeBtn = (Button)findViewById(R.id.button_time);

        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int min = c.get(Calendar.MINUTE);

                tpd = new TimePickerDialog(appointmentScheduling.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int mHour, int mMin) {
                        String AM_PM = "AM";
                        if(mHour > 12) {
                            mHour -= 12;
                            AM_PM = "PM";
                        }
                        timeBtn.setText(String.format("%02d:%02d %s", mHour, mMin, AM_PM));
                    }
                }, hour, min, false);
                tpd.show();
            }
        });

        schedBtn = (Button)findViewById(R.id.button_appt_submit);
        schedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(appointmentScheduling.this, "Appointment Created", Toast.LENGTH_SHORT).show();
                Intent intent;
                intent = new Intent(appointmentScheduling.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
