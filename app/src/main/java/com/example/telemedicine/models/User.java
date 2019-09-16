package com.example.telemedicine.models;

public class User {

    // Global Variables
    String firstName, lastName, email, password;
    int lastFourSSN;

    // Constructor
    public User(String firstName, String lastName, String email, String password, int lastFourSSN) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.lastFourSSN = lastFourSSN;
    }

    // Accessor Methods
    public String getFirstName() {
        return this.firstName;
    }
    public String getLastName() {
        return this.lastName;
    }
    public String getEmail() {
        return this.email;
    }
    public String getPassword() {
        return this.password;
    }
    public int getLastFourSSN() {
        return this.lastFourSSN;
    }

    // Mutator Methods
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setLastFourSSN(int lastFourSSN) { this.lastFourSSN = lastFourSSN; }
}
