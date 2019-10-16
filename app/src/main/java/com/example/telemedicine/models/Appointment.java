package com.example.telemedicine.models;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Appointment {

    //Global Variables
    private String apptID, userID, doctorID, ampm, type;
    private int apptYear, apptMonth, apptDay, apptHour, apptMin;

    public Appointment(){

    }

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
        this.type = type;
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

    @Override
    public String toString() {
        return "Appointment{" +
                "apptID='" + apptID + '\'' +
                ", userID='" + userID + '\'' +
                ", doctorID='" + doctorID + '\'' +
                ", ampm='" + ampm + '\'' +
                ", type='" + type + '\'' +
                ", apptYear=" + apptYear +
                ", apptMonth=" + apptMonth +
                ", apptDay=" + apptDay +
                ", apptHour=" + apptHour +
                ", apptMin=" + apptMin +
                '}';
    }

    public String shortString() {
        String ss = String.format("%s - %02d/%02d @ %02d:%02d %s", type, apptMonth, apptDay, apptHour, apptMin, ampm);
        return ss;
    }
}
