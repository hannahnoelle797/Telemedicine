package com.example.telemedicine.models;

public class Report {
    private String reportID, userID, reportName, url, read;

    public  Report() {}

    public Report(String reportID, String userID, String reportName, String url, String read) {
        this.reportID = reportID;
        this.userID = userID;
        this.reportName = reportName;
        this.url = url;
        this.read = read;
    }

    public String getReportID(){ return reportID; }

    public String getUserID() {
        return userID;
    }

    public String getReportName() {
        return reportName;
    }

    public String getUrl() {
        return url;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

}
