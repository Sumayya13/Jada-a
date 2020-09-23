package com.example.jadaa.ui.college;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CollegeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CollegeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is College Page");
    }

    public LiveData<String> getText() {
        return mText;
    }
}