package com.example.telemedicine.ui.appointments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AppointmentsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AppointmentsViewModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}