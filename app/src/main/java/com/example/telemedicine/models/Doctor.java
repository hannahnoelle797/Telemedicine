package com.example.telemedicine.models;

public class Doctor {

    String fullName, email, street, phoneNum, gender, docID, docString; //docString is concatenation of "Dr. " + firstName + lastName
    int empNum;

    public Doctor(){
        docID = "";
        fullName = "";
        email = "";
        street = "";
        phoneNum = "";
        gender = "";
        docString = "";
        empNum = 0;
    }

    public Doctor(String docID, String fullName, String email, String streetAddress, String phoneNum, String gender, String docString, int empNum) {
        this.docID = docID;
        this.fullName = fullName;
        this.email = email;
        this.street = streetAddress;
        this.phoneNum = phoneNum;
        this.gender = gender;
        this.docString = docString;
        this.empNum = empNum;
    }

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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
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

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public String getDocString() {
        return docString;
    }

    public void setDocString(String docString) {
        this.docString = docString;
    }

    public int getEmpNum() {
        return empNum;
    }

    public void setEmpNum(int empNum) {
        this.empNum = empNum;
    }
}