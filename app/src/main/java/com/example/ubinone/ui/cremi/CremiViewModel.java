package com.example.ubinone.ui.cremi;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CremiViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CremiViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is cremi fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}