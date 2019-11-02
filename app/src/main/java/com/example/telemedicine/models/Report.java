package com.example.telemedicine.models;

public class Report {
    private String reportID, userID, reportName, url;

    public  Report() {}

    public Report(String reportID, String userID, String reportName, String url) {
        this.reportID = reportID;
        this.userID = userID;
        this.reportName = reportName;
        this.url = url;
    }

    public String getReportID(){ return reportID; }

    public void setReportID(String reportID){ this.reportID = reportID; }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
