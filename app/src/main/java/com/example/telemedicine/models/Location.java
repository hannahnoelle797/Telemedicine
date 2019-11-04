package com.example.telemedicine.models;

public class Location {
    private String address, city, name, state, street, id, phone;
    private String latitude, longitude, latlong;
    private int zip;

    public Location() {

    }

    public Location(String id, String address, String street, String city, String state, String name, String phone, String lat, String longitude, String latlong, int zip) {
        this.id = id;
        this.address = address;
        this.city = city;
        this.name = name;
        this.phone = phone;
        this.state = state;
        this.street = street;
        this.latitude = lat;
        this.longitude = longitude;
        this.latlong = latlong;
        this.zip = zip;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getZipcode() {
        return zip;
    }

    public void setZipcode(int zipcode) {
        this.zip = zipcode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLatlong() {
        return latlong;
    }

    public void setLatlong(String latlong) {
        this.latlong = latlong;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }
}