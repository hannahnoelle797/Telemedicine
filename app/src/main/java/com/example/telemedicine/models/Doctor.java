package com.example.telemedicine.models;

public class Doctor {

    String firstName, lastName, email, password, docID, docString; //docString is concatenation of "Dr. " + firstName + lastName
    int empNum;

    public Doctor(String firstName, String lastName, String email, String password, String docID, String docString, int empNum) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.docID = docID;
        this.docString = docString;
        this.empNum = empNum;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
