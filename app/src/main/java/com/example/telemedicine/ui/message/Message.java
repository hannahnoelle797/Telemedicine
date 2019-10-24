package com.example.telemedicine.ui.message;
 import java.util.Date;
public class Message {
    private String mText;
    private String mSender;
    private Date mDate;

    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mDate) { this.mDate = mDate; }
    public String getmSender() {return mSender;}

    public void setmSender(String mSender) {this.mSender=mSender;}
    public String getmText() {return mText;}

    public void setmText(String mText) {this.mText=mText; }

    public void setSender(String s) {
    }

    public void setText(String s) {
    }

    public void setDate(Date parse) {
    }
}
