package com.example.telemedicine;

import android.app.Application;

import com.firebase.client.Firebase;

public class AppListApp extends Application {

    protected void OnCreate(){
        super.onCreate();
        Firebase.setAndroidContext(this);

}


    }
