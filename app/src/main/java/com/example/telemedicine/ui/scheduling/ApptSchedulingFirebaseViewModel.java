package com.example.telemedicine.ui.scheduling;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ApptSchedulingFirebaseViewModel {
    private MutableLiveData<String> mText;

    public ApptSchedulingFirebaseViewModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}