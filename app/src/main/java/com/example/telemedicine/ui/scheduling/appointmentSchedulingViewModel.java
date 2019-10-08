package com.example.telemedicine.ui.scheduling;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class appointmentSchedulingViewModel {
    private MutableLiveData<String> mText;

    public appointmentSchedulingViewModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}