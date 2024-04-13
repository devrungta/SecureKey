package com.example.securekey;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyViewModel extends ViewModel {
    private MutableLiveData<String> editTextValue = new MutableLiveData<>();

    public void setEditTextValue(String value) {
        editTextValue.setValue(value);
    }

    public LiveData<String> getEditTextValue() {
        return editTextValue;
    }
}
