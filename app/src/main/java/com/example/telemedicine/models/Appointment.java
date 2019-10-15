package com.example.telemedicine.models;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Appointment {

    //Global Variables
    private String apptID, userID, doctorID, ampm, type;
    private Calendar apptDate;
    private int apptYear, apptMonth, apptDay, apptHour, apptMin;

    public Appointment(String apptId, String userId, String doctorId, int apptYear, int apptMonth, int apptDay, int apptHour, int apptMin, String ampm, String type)
    {
        this.apptID = apptId;
        this.userID = userId;
        this.doctorID = doctorId;
        this.apptYear = apptYear;
        this.apptMonth = apptMonth;
        this.apptDay = apptDay;
        this.apptHour = apptHour;
        this.apptMin = apptMin;
        this.ampm = ampm.toUpperCase();
        apptDate = Calendar.getInstance((TimeZone.getTimeZone("GMT-4")), Locale.US);
        apptDate.set(apptYear, apptMonth, apptDay, apptHour, apptMin);
        this.type = type;
    }

    public void setDate()
    {
        apptDate = Calendar.getInstance((TimeZone.getTimeZone("GMT-4")), Locale.US);
        apptDate.set(apptYear, apptMonth, apptDay, apptHour, apptMin);
    }

    public String getApptID() {
        return apptID;
    }

    public void setApptID(String apptID) {
        this.apptID = apptID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public int getApptYear() {
        return apptYear;
    }

    public void setApptYear(int apptYear) {
        this.apptYear = apptYear;
    }

    public int getApptMonth() {
        return apptMonth;
    }

    public void setApptMonth(int apptMonth) {
        this.apptMonth = apptMonth;
    }

    public int getApptDay() {
        return apptDay;
    }

    public void setApptDay(int apptDay) {
        this.apptDay = apptDay;
    }

    public int getApptHour() {
        return apptHour;
    }

    public void setApptHour(int apptHour) {
        this.apptHour = apptHour;
    }

    public int getApptMin() {
        return apptMin;
    }

    public void setApptMin(int apptMin) {
        this.apptMin = apptMin;
    }

    public String getAmpm() {
        return ampm;
    }

    public void setAmpm(String ampm) {
        this.ampm = ampm;
    }

    public String getType() {return type;}

    public void setType(String type) {this.type = type;}
}
