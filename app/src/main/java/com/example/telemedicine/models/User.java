package com.example.telemedicine.models;

public class User {

    // Global Variables
    String fullName, email, password, userID, address, phoneNum, gender;
    int lastFourSSN;

    // Constructor
    public User(String firebaseUserID, String fullName, String email, String streetAddress, String phoneNum, String gender, int lastFourSSN) {
        this.userID = firebaseUserID;
        this.fullName = fullName;
        this.email = email;
        this.address = streetAddress;
        this.phoneNum = phoneNum;
        this.gender = gender;
        this.lastFourSSN = lastFourSSN;
    }

    // Default Constructor
    public User() {}

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getLastFourSSN() {
        return lastFourSSN;
    }

    public void setLastFourSSN(int lastFourSSN) {
        this.lastFourSSN = lastFourSSN;
    }
}